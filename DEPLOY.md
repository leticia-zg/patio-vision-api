
# (1) Registrar provedores necessários (habilita tipos de recurso)
az provider register --namespace Microsoft.Web
az provider register --namespace Microsoft.Insights
az provider register --namespace Microsoft.OperationalInsights
az provider register --namespace Microsoft.ServiceLinker
az provider register --namespace Microsoft.DBforPostgreSQL

# (2) Variáveis de ambiente 
RG="rg-patio-vision"
LOCATION="brazilsouth"
PGNAME="pg-rm556219"
PGADMIN="pgadmin"
PGADMINPWD="patio-vision"
DBNAME="patio_vision"
APP_INSIGHTS_NAME="ai-$PGNAME"
APP_SERVICE_PLAN="plan-$PGNAME"
WEBAPP_NAME="app-$PGNAME"
PGHOST="${PGNAME}.postgres.database.azure.com"

# (3) Criar Resource Group
az group create -n "$RG" -l "$LOCATION"

# (4) Criar PostgreSQL Flexible Server 
az postgres flexible-server create \
  -g "$RG" -n "$PGNAME" -l "$LOCATION" \
  --tier Burstable --sku-name Standard_B1ms \
  --storage-size 32 \
  --version 16 \
  --admin-user "$PGADMIN" --admin-password "$PGADMINPWD" \
  --public-access 0.0.0.0-255.255.255.255

# (5) Criar database dentro do servidor Postgres
az postgres flexible-server db create \
  --resource-group "$RG" \
  --server-name "$PGNAME" \
  --database-name "$DBNAME"

# (6) Instalar extensão do Application Insights
az extension add --name application-insights || true

# (7) Criar recurso Application Insights
az monitor app-insights component create \
  --app "$APP_INSIGHTS_NAME" \
  --location "$LOCATION" \
  --resource-group "$RG" \
  --application-type web

# (8) Capturar a connection string do App Insights 
APPINSIGHTS_CONN=$(az monitor app-insights component show \
  -g "$RG" --app "$APP_INSIGHTS_NAME" \
  --query connectionString -o tsv)

echo "APPINSIGHTS_CONNECTION_STRING=$APPINSIGHTS_CONN"

# (9) Criar App Service Plan 
az appservice plan create \
  --name "$APP_SERVICE_PLAN" \
  --resource-group "$RG" \
  --location "$LOCATION" \
  --sku F1 \
  --is-linux

# (10) Criar Web App
az webapp create \
  --name "$WEBAPP_NAME" \
  --resource-group "$RG" \
  --plan "$APP_SERVICE_PLAN" \
  --runtime "JAVA:17-java17"

# (11) Configurar App Settings
az webapp config appsettings set \
  --name "$WEBAPP_NAME" \
  --resource-group "$RG" \
  --settings \
    APPLICATIONINSIGHTS_CONNECTION_STRING="$APPINSIGHTS_CONN" \
    ApplicationInsightsAgent_EXTENSION_VERSION="~3" \
    XDT_MicrosoftApplicationInsights_Mode="Recommended" \
    XDT_MicrosoftApplicationInsights_PreemptSdk="1" \
    SPRING_DATASOURCE_URL="jdbc:postgresql://$PGHOST:5432/$PG_DBNAME?sslmode=require" \
    SPRING_DATASOURCE_USERNAME="pgadmin" \
    SPRING_DATASOURCE_PASSWORD="$PGADMINPWD" \
    SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect" \
    SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.postgresql.Driver"

# (12) Conecta o Web App do Azure ao repositório do GitHub e cria um workflow do GitHub Actions
az webapp deployment github-actions add --name "$WEBAPP_NAME" --resource-group "$RG" --repo "leticia-zg/patio-vision-api" --branch "main" --login-with-github

# (13) Valide
az webapp restart -g "$RG" -n "$WEBAPP_NAME"
az webapp log tail -g "$RG" -n "$WEBAPP_NAME"

# (14) Rode o link do App Service
app-pg-rm556219.azurewebsites.net


# Obrigatório para o deploy (Settings > Secrets and variables > Actions)
# - AZURE_WEBAPP_PUBLISH_PROFILE  -> Publish Profile baixado do App Service no portal
# - SPRING_DATASOURCE_URL
# - SPRING_DATASOURCE_USERNAME
# - SPRING_DATASOURCE_PASSWORD
