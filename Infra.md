# ✅ INFRA ACR — Criação do Azure Container Registry

###
### Variáveis
###
grupoRecursos="rg-patiovision"
regiao="eastus"        # Outras opções: brazilsouth | eastus2 | westus | westus2
rm="rm558090"
nomeACR="acr$rm"
skuACR="Basic"

###
### Criação do Resource Group
###
if [ "$(az group exists --name $grupoRecursos)" = true ]; then
    echo "🔎 O grupo de recursos $grupoRecursos já existe."
else
    az group create --name "$grupoRecursos" --location "$regiao"
    echo "✅ Grupo de recursos $grupoRecursos criado na região $regiao."
fi

###
### Criação do Azure Container Registry
###
if az acr show --name "$nomeACR" --resource-group "$grupoRecursos" &> /dev/null; then
    echo "🔎 O ACR $nomeACR já existe."
else
    az acr create --resource-group "$grupoRecursos" --name "$nomeACR" --sku "$skuACR"
    az acr update --name "$nomeACR" --resource-group "$grupoRecursos" --admin-enabled true
    echo "✅ ACR $nomeACR criado e Admin User habilitado."
fi

###
### Credenciais (somente para testes)
###
ADMIN_USER=$(az acr credential show --name "$nomeACR" --query "username" -o tsv)
ADMIN_PASSWORD=$(az acr credential show --name "$nomeACR" --query "passwords[0].value" -o tsv)

export ACR_ADMIN_USER="$ADMIN_USER"
export ACR_ADMIN_PASSWORD="$ADMIN_PASSWORD"

echo "🔐 Usuário ACR: $ACR_ADMIN_USER"
echo "🔐 Senha ACR: $ACR_ADMIN_PASSWORD"


# ✅ INFRA BANCO — PostgreSQL Flexible Server


# Registrar provedores necessários
az provider register --namespace Microsoft.DBforPostgreSQL

###
### Variáveis
###
RG="rg-patiovision"
LOCATION="brazilsouth"
PGNAME="pg-rm558090"
PGADMIN="pgadmin"
PGADMINPWD="patio-vision"
DBNAME="patio_vision"
PGHOST="${PGNAME}.postgres.database.azure.com"

###
### Criar RG (caso não exista)
###
az group create -n "$RG" -l "$LOCATION"

###
### Criar PostgreSQL Flexible Server
###
az postgres flexible-server create \
  -g "$RG" -n "$PGNAME" -l "$LOCATION" \
  --tier Burstable --sku-name Standard_B1ms \
  --storage-size 32 \
  --version 16 \
  --admin-user "$PGADMIN" \
  --admin-password "$PGADMINPWD" \
  --public-access 0.0.0.0-255.255.255.255

###
### Criar Database
###
az postgres flexible-server db create \
  --resource-group "$RG" \
  --server-name "$PGNAME" \
  --database-name "$DBNAME"

echo "✅ BANCO CRIADO COM SUCESSO!"
echo "HOST      = $PGHOST"
echo "DATABASE  = $DBNAME"
echo "USER      = $PGADMIN"
echo "PASSWORD  = $PGADMINPWD"


# ✅ INFRA ACI — Deploy do Container + Web App

###
### Variáveis
###
grupoRecursos="rg-patiovision"
rm="rm558090"

nomeACR="acr$rm"
serverACR="acr$rm.azurecr.io"
imageACR="acr$rm.azurecr.io/acr:latest"

userACR=$(az acr credential show --name "$nomeACR" --query "username" -o tsv)
passACR=$(az acr credential show --name "$nomeACR" --query "passwords[0].value" -o tsv)

nomeACI="aci$rm"
regiao="eastus"
planService="planACRWebApp"
sku="F1"
appName="acrwebapp$rm"
port=80

###
### Criar Container no ACI
###
az container create \
    --resource-group "$grupoRecursos" \
    --name "$nomeACI" \
    --image "$imageACR" \
    --cpu 1 \
    --memory 1 \
    --os-type Linux \
    --registry-login-server "$serverACR" \
    --registry-username "$userACR" \
    --registry-password "$passACR" \
    --dns-name-label "$nomeACI" \
    --restart-policy Always \
    --ports 8080

###
### Criar App Service Plan (caso não exista)
###
if az appservice plan show --name "$planService" --resource-group "$grupoRecursos" &> /dev/null; then
    echo "🔎 O plano de serviço $planService já existe."
else
    az appservice plan create --name "$planService" --resource-group "$grupoRecursos" --is-linux --sku "$sku"
    echo "✅ Plano de serviço $planService criado."
fi

###
### Criar Web App (caso não exista)
###
if az webapp show --name "$appName" --resource-group "$grupoRecursos" &> /dev/null; then
    echo "🔎 O Web App $appName já existe."
else
    az webapp create \
      --resource-group "$grupoRecursos" \
      --plan "$planService" \
      --name "$appName" \
      --deployment-container-image-name "$imageACR"
    echo "✅ Web App $appName criado."
fi

###
### Configurar porta do Web App
###
az webapp config appsettings set \
  --resource-group "$grupoRecursos" \
  --name "$appName" \
  --settings WEBSITES_PORT=$port

echo "✅ Web App configurado para porta $port"
echo "🎉 Deploy completo!"
