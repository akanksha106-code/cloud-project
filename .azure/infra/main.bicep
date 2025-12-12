param environmentName string
param location string = resourceGroup().location

var resourceToken = uniqueString(subscription().id, resourceGroup().id, location, environmentName)

// User Assigned Managed Identity
resource uami 'Microsoft.ManagedIdentity/userAssignedIdentities@2023-01-31' = {
  name: 'azumi${resourceToken}'
  location: location
  tags: {
    'azd-env-name': environmentName
  }
}

// Key Vault
resource keyVault 'Microsoft.KeyVault/vaults@2023-07-01' = {
  name: 'azkv${resourceToken}'
  location: location
  properties: {
    sku: {
      family: 'A'
      name: 'standard'
    }
    tenantId: subscription().tenantId
    accessPolicies: []
    enabledForDeployment: false
    enabledForTemplateDeployment: false
    enabledForDiskEncryption: false
    enableRbacAuthorization: true
    publicNetworkAccess: 'Enabled'
  }
  tags: {
    'azd-env-name': environmentName
  }
}

// Role assignment for UAMI to Key Vault Secrets Officer
resource keyVaultRoleAssignment 'Microsoft.Authorization/roleAssignments@2022-04-01' = {
  name: guid(resourceGroup().id, uami.id, 'b86a8fe4-44ce-4948-aee5-eccb2c155cd7')
  scope: keyVault
  properties: {
    principalId: uami.properties.principalId
    roleDefinitionId: subscriptionResourceId('Microsoft.Authorization/roleDefinitions', 'b86a8fe4-44ce-4948-aee5-eccb2c155cd7')
  }
  dependsOn: [
    keyVault
    uami
  ]
}

// SQL Server
resource sqlServer 'Microsoft.Sql/servers@2022-08-01-preview' = {
  name: 'azsql${resourceToken}'
  location: location
  properties: {
    administratorLogin: 'sqladmin'
    administratorLoginPassword: 'CloudApp2025!'
    version: '12.0'
    publicNetworkAccess: 'Enabled'
  }
  tags: {
    'azd-env-name': environmentName
  }
}

// SQL Database
resource sqlDatabase 'Microsoft.Sql/servers/databases@2022-08-01-preview' = {
  parent: sqlServer
  name: 'azsqldb${resourceToken}'
  location: location
  sku: {
    name: 'Basic'
    tier: 'Basic'
  }
  properties: {
    collation: 'SQL_Latin1_General_CP1_CI_AS'
  }
  tags: {
    'azd-env-name': environmentName
  }
}

// App Service Plan
resource appServicePlan 'Microsoft.Web/serverfarms@2022-09-01' = {
  name: 'azasp${resourceToken}'
  location: location
  sku: {
    name: 'F1'
    tier: 'Free'
  }
  properties: {
    reserved: true
  }
  tags: {
    'azd-env-name': environmentName
  }
}

// Application Insights
resource appInsights 'Microsoft.Insights/components@2020-02-02' = {
  name: 'azai${resourceToken}'
  location: location
  kind: 'web'
  properties: {
    Application_Type: 'web'
    WorkspaceResourceId: logAnalytics.id
  }
  tags: {
    'azd-env-name': environmentName
  }
}

// Log Analytics Workspace
resource logAnalytics 'Microsoft.OperationalInsights/workspaces@2022-10-01' = {
  name: 'azla${resourceToken}'
  location: location
  properties: {
    sku: {
      name: 'PerGB2018'
    }
    retentionInDays: 30
  }
  tags: {
    'azd-env-name': environmentName
  }
}

// App Service
resource appService 'Microsoft.Web/sites@2022-09-01' = {
  name: 'azapp${resourceToken}'
  location: location
  identity: {
    type: 'UserAssigned'
    userAssignedIdentities: {
      '${uami.id}': {}
    }
  }
  properties: {
    serverFarmId: appServicePlan.id
    siteConfig: {
      linuxFxVersion: 'JAVA|17-java17'
      cors: {
        allowedOrigins: ['*']
      }
      appSettings: [
        {
          name: 'SPRING_PROFILES_ACTIVE'
          value: 'azure'
        }
        {
          name: 'AZURE_SQL_URL'
          value: 'jdbc:sqlserver://${sqlServer.properties.fullyQualifiedDomainName}:1433;database=${sqlDatabase.name};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;'
        }
        {
          name: 'AZURE_SQL_USERNAME'
          value: 'sqladmin'
        }
        {
          name: 'AZURE_SQL_PASSWORD'
          value: 'CloudApp2025!'
        }
        {
          name: 'APPLICATIONINSIGHTS_CONNECTION_STRING'
          value: appInsights.properties.ConnectionString
        }
      ]
    }
  }
  tags: {
    'azd-service-name': 'dbs-app'
    'azd-env-name': environmentName
  }
  dependsOn: [
    appServicePlan
    sqlServer
    sqlDatabase
    appInsights
    uami
  ]
}

// Diagnostic Settings for App Service
resource appServiceDiagnostics 'Microsoft.Insights/diagnosticSettings@2021-05-01-preview' = {
  name: 'appServiceDiagnostics'
  scope: appService
  properties: {
    workspaceId: logAnalytics.id
    logs: [
      {
        category: 'AppServiceHTTPLogs'
        enabled: true
      }
      {
        category: 'AppServiceConsoleLogs'
        enabled: true
      }
      {
        category: 'AppServiceAppLogs'
        enabled: true
      }
    ]
    metrics: [
      {
        category: 'AllMetrics'
        enabled: true
      }
    ]
  }
  dependsOn: [
    appService
    logAnalytics
  ]
}

output RESOURCE_GROUP_ID string = resourceGroup().id