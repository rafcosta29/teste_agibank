# Teste Agibank

Este repositório contém três tipos de testes automatizados:

1. **Web Test**: Testes de interface utilizando Selenium e JUnit.
2. **API Test**: Testes unitários de API utilizando JUnit e Allure para relatórios.
3. **Performance Test**: Testes de carga utilizando JMeter, gerando arquivos `.csv` como saída.

## Pré-requisitos

Antes de executar os testes, certifique-se de ter instalado:

- **Java 17** ou superior
- **Maven**
- **Google Chrome** (para testes web)
- **ChromeDriver** (compatível com a versão do Chrome instalada)
- **JMeter** (para testes de performance)
- **Git** (para clonar o repositório)

## Clonando o Repositório

```sh
 git clone https://github.com/rncosta29/teste_agibank.git
 cd teste_agibank
```

---

## **1. Web Test**

### Instalação de Dependências

Acesse a pasta `webtest` e execute:

```sh
cd webtest
mvn clean install
```

### Configuração do ChromeDriver

1. Baixe a versão correta do [ChromeDriver](https://googlechromelabs.github.io/chrome-for-testing/)
2. Extraia e coloque o executável no caminho do sistema ou configure no código.

### Executando os Testes

```sh
mvn test
```

Os testes acessarão uma página HTML e executarão as verificações necessárias.

---

## **2. API Test**

### Instalação de Dependências

Acesse a pasta `apitest` e execute:

```sh
cd apitest
mvn clean install
```

### Executando os Testes

```sh
mvn test
```

### Gerando o Relatório com ExtentReports

Após a execução dos testes, o relatório será gerado automaticamente na pasta `target/reports/`.

Para visualizar o relatório, abra o arquivo HTML gerado no navegador:

```sh
open target/extent-report.html
```

Isso abrirá um relatório detalhado no navegador.

---

## **3. Performance Test**

### Configuração do JMeter

1. Baixe e instale o [Apache JMeter](https://jmeter.apache.org/)
2. Adicione o caminho do JMeter ao `PATH` do sistema.

### Executando os Testes de Performance

Acesse a pasta `performancetest/bot` e execute os arquivos `.jmx`:

```sh
jmeter -n -t loadTest.jmx -l ../results/results_loadtest.csv
jmeter -n -t spikeTest.jmx -l ../results/results_spiketest.csv 
```

Isso gerará arquivos `.csv` com os resultados dos testes.

---

### Gerando Relatórios no JMeter

Após a execução dos testes, gere os relatórios em formato dashboard executando:

```sh
jmeter -g ../results/results_loadtest.csv -o ../results/loadtest_dashboard
jmeter -g ../results/results_spiketest.csv -o ../results/spiketest_dashboard
```

Isso criará dashboards interativos dentro das pastas `loadtest_dashboard` e `spiketest_dashboard`.

---

## **Gerando e Disponibilizando Relatórios**

Para disponibilizar os relatórios como artefatos:

1. Configure um workflow no GitHub Actions para armazenar os arquivos `.csv` e dashboards gerados pelo JMeter.

---

## **Contato**
Caso tenha dúvidas ou sugestões, abra uma issue neste repositório!
