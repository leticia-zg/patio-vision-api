# Infra ACR:

###
### Variáveis
###
grupoRecursos=rg-patiovision
# Altere a Região conforme orientação do Prof
regiao=eastus
#Outras opções:
#brazilsouth
#eastus2
#westus
#westus2
# Altere para seu RM
rm=rm558090
nomeACR="acr$rm"
skuACR=Basic

###
### Criação do Grupo de Recursos
###
# Verifica a existência do grupo de recursos
if [ $(az group exists --name $grupoRecursos) = true ]; then
    echo "O grupo de recursos $grupoRecursos já existe"
else
    # Cria o grupo de recursos
    az group create --name $grupoRecursos --location $regiao
    echo "Grupo de recursos $grupoRecursos criado na localização $regiao"
fi

###
### Criação do Azure Container Registry
###
# Verifica se o ACR já existe
if az acr show --name $nomeACR --resource-group $grupoRecursos &> /dev/null; then
    echo "O ACR $nomeACR já existe"
else
    # Cria o ACR
    az acr create --resource-group $grupoRecursos --name $nomeACR --sku $skuACR
    echo "ACR $nomeACR criado com sucesso"
    # Habilita o Usuário Administrador no Azure Container Registry
    az acr update --name $nomeACR --resource-group $grupoRecursos --admin-enabled true
    echo "Habilitado com sucesso o usuário Administrador para o ACR $nomeACR"
fi

#
# Essa parte do Script só é recomendada para fins de testes e aprendizado
#
# WARNING: [Warning] This output may compromise security by showing secrets. Learn more at: https://go.microsoft.com/fwlink/?linkid=2258669
#
# Recuperar as credenciais do usuário administrador, armazenar em variáveis de ambiente e mostrar as credenciais
ADMIN_USER=$(az acr credential show --name $nomeACR --query "username" -o tsv)
ADMIN_PASSWORD=$(az acr credential show --name $nomeACR --query "passwords[0].value" -o tsv)

# Cria variáveis de ambiente
export ACR_ADMIN_USER=$ADMIN_USER
export ACR_ADMIN_PASSWORD=$ADMIN_PASSWORD

# Mostra as variáveis de ambiente
echo $ACR_ADMIN_USER
echo $ACR_ADMIN_PASSWORD


# Banco:
# (1) Registrar provedores necessários
az provider register --namespace Microsoft.DBforPostgreSQL

# (2) Variáveis
RG="rg-patiovision"
LOCATION="brazilsouth"
PGNAME="pg-rm558090"
PGADMIN="pgadmin"
PGADMINPWD="patio-vision"
DBNAME="patio_vision"
PGHOST="${PGNAME}.postgres.database.azure.com"

# (3) Criar Resource Group (caso não exista)
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

echo "BANCO CRIADO COM SUCESSO!"
echo "HOST = $PGHOST"
echo "DATABASE = $DBNAME"
echo "USER = $PGADMIN"
echo "PASSWORD = $PGADMINPWD"


# Infra ACI:

###
### Variáveis
###
grupoRecursos=rg-patiovision
# Altere para seu RM
rm=rm558090
nomeACR="acr$rm"
imageACR="acr$rm.azurecr.io/acr:latest"
serverACR="acr$rm.azurecr.io"
userACR=$(az acr credential show --name $nomeACR --query "username" -o tsv)
passACR=$(az acr credential show --name $nomeACR --query "passwords[0].value" -o tsv)
nomeACI="aci$rm"
# Altere a Região conforme orientação do Prof
regiao=eastus
#Outras opções:
#brazilsouth
#eastus2
#westus
#westus2
planService=planACRWebApp
sku=F1
appName="acrwebapp$rm"
imageACR="acr$rm.azurecr.io/acr:latest"
port=80

###
### Criação do ACI
###
az container create \
    --resource-group $grupoRecursos \
    --name $nomeACI \
    --image $imageACR \
    --cpu 1 \
    --memory 1 \
    --os-type Linux \
    --registry-login-server $serverACR \
    --registry-username $userACR \
    --registry-password $passACR \
    --dns-name-label $nomeACI \
    --restart-policy Always \
    --ports 8080

###
### Criação do Web App
###

### Cria o Plano de Serviço se não existir
if az appservice plan show --name $planService --resource-group $grupoRecursos &> /dev/null; then
    echo "O plano de serviço $planService já existe"
else
    az appservice plan create --name $planService --resource-group $grupoRecursos --is-linux --sku $sku
    echo "Plano de serviço $planService criado com sucesso"
fi 
### Cria o Serviço de Aplicativo se não existir
if az webapp show --name $appName --resource-group $grupoRecursos &> /dev/null; then
    echo "O Serviço de Aplicativo $appName já existe"
else
    az webapp create --resource-group $grupoRecursos --plan $planService --name  $appName --deployment-container-image-name $imageACR
    echo "Serviço de Aplicativo $appName criado com sucesso"
fi
### Cria uma variável definindo a porta do Serviço de Aplicativo
if az webapp show --name $appName --resource-group $grupoRecursos > /dev/null 2>&1; then
    az webapp config appsettings set --resource-group $grupoRecursos --name $appName --settings WEBSITES_PORT=$port
    echo "Serviço de Aplicativo $appName configurado para escutar na porta $port com sucesso"
fi
