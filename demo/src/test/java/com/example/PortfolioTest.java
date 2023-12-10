package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class PortfolioTest {

    final private String url = "https://dev.e-rentav.com/";
    final private String loginEmail = "daniel.colao.700@ufrn.edu.br";
    final private String loginPassword = ")#Un)MsI0";

    private WebDriver webDriver = new ChromeDriver();
    
    @BeforeClass
    public static void setup(){
        String driverPath = "D:\\Projetos\\selenium-java\\demo\\src\\main\\java\\com\\example\\driver\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);
    }

    @Test
    public void cadastrarPortfolioComSuceso(){
        // Gerar descrição
        String descricao = UUID.randomUUID().toString();

        String simbolo = descricao.substring(0, 4);
        int quantidade = 7;
        double valorTotal = 70;
        LocalDate dataVencimento = LocalDate.now().plusDays(1);
        realizarCadastroDePortfolio(descricao, dataVencimento, simbolo, quantidade, valorTotal);

        WebElement alerElement = webDriver.findElement(By.className("notifications"));
        WebElement spanElement = alerElement.findElement(By.xpath(".//span/div/span[2]/span"));
        System.out.println("mensagem apresentada: " + spanElement.getText());
        assertEquals("Portfolio adicionado!", spanElement.getText());

        // Cancelar para voltar para listagem
        WebElement cancelarBtn = webDriver.findElement(By.className("btn-outline-secondary"));
        cancelarBtn.click();

        // Verificar se a portfólio está presente na listagem
        assertTrue(validarCadastroPortfolio(descricao, quantidade, valorTotal));
        webDriver.quit();
    }

    @Test
    public void cadastrarPortfolioQuantidadeNegativa(){
        // Gerar descrição
        String descricao = UUID.randomUUID().toString();

        String simbolo = "";
        int quantidade = -10;
        double valorTotal = 100;
        LocalDate dataVencimento = LocalDate.now().plusDays(1);
        realizarCadastroDePortfolio(descricao, dataVencimento, simbolo, quantidade, valorTotal);

        // Validar se os campos símbolo e quantidade estão apresentando erro
        WebElement simboloElmnt = webDriver.findElement(By.xpath("//*[@id=\"portfolio\"]/div[1]/div/div[6]"));
        assertTrue("Campo simbolo sem classe de  erro", simboloElmnt.getAttribute("class").contains("has-error"));
        WebElement quantidadeElmnt = webDriver.findElement(By.xpath("//*[@id=\"portfolio\"]/div[1]/div/div[7]"));
        assertTrue("Campo quantidade sem classe de erro", quantidadeElmnt.getAttribute("class").contains("has-error"));

        // Cancelar para voltar para listagem
        WebElement cancelarBtn = webDriver.findElement(By.className("btn-outline-secondary"));
        cancelarBtn.click();
        // Verificar se a portfólio não está presente na listagem
        assertFalse(validarCadastroPortfolio(descricao, quantidade, valorTotal));

        webDriver.quit();
    }

    private void realizarCadastroDePortfolio(String descricao, LocalDate dataVencimento, String simbolo, int quantidade, double valorTotal){
        webDriver.get(url);
        
        // Realizar login no sistema
        realizarLogin();
        sleep(2000);

        // Clicar no botão "Portfolio"
        WebElement portfolioMenuLateral = webDriver.findElement(By.xpath("//*[@id=\"sidenav-collapse-main\"]/ul/li[3]/a"));
        portfolioMenuLateral.click();

        sleep(1000);

        // Clicar no botão "Adicionar novo"
        WebElement adicionarNovoBtn = webDriver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/a"));
        adicionarNovoBtn.click();
        sleep(1000);
        preencherFormularioPortfolio(descricao, dataVencimento, simbolo, quantidade, valorTotal);
        sleep(2000);
    }

    private void realizarLogin(){
        // Inserção de E-mail
        WebElement emailElement = webDriver.findElement(By.xpath("//*[@id=\"login\"]/div[1]/div/input"));
        emailElement.sendKeys(loginEmail);
        // Inserção de Senha
        WebElement passwordElement = webDriver.findElement(By.xpath("//*[@id=\"login\"]/div[2]/div/input"));
        passwordElement.sendKeys(loginPassword);
        // Clicar no botão de login
        WebElement loginBtn = webDriver.findElement(By.xpath("//*[@id=\"login\"]/div[3]/div[2]/button"));
        loginBtn.submit();
    }

    private void preencherFormularioPortfolio(String descricao, LocalDate dataVencimento, String simbolo, int quantidade, double valorTotal){
        // Selecionar conta
        // Abrir select de contas
        WebElement contas = webDriver.findElement(By.xpath("//*[@id=\"portfolio\"]/div[1]/div/div[2]/div/div/div/input"));
        contas.click();
        // Obter listagem de contas no select
        WebElement dropdown = webDriver.findElement(By.xpath("//*[@id=\"leftMenu\"]/div[3]/div[1]/div[1]/ul"));
        // Selecionar primeira conta disponível
        WebElement item = dropdown.findElement(By.xpath(".//li[1]"));
        item.click();

        // Digitar descrição
        WebElement descricaoElmnt = webDriver.findElement(By.id("description"));
        descricaoElmnt.sendKeys(descricao);

        // Digitar data de vencimento
        WebElement vencimentoElmnt = webDriver.findElement(By.id("expiration_date"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        vencimentoElmnt.sendKeys(formatter.format(dataVencimento));

        // Digitar símbolo
        WebElement simboloElmnt = webDriver.findElement(By.id("symbol"));
        simboloElmnt.sendKeys(simbolo);

        // Digitar quantidade
        WebElement quantidadeElmnt = webDriver.findElement(By.id("quantity"));
        quantidadeElmnt.sendKeys(String.valueOf(quantidade));

        // Digitar valor total
        WebElement totalElmnt = webDriver.findElement(By.name("value"));
        totalElmnt.sendKeys(String.format(Locale.ENGLISH, "%.2f", valorTotal));

        // Clicar no botão de confirmar cadastro
        WebElement salvarBtn = webDriver.findElement(By.className("btn-success"));
        salvarBtn.click();
    }
    
    private boolean validarCadastroPortfolio(String descricao, int quantidade, double valorTotal){
        sleep(1500);

        WebElement table = webDriver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/table/tbody"));
        List<WebElement> tableRows = table.findElements(By.xpath(".//tr"));
        for (WebElement row : tableRows) {
            WebElement rowDescriptionElement = row.findElement(By.xpath(".//td[1]"));
            if(rowDescriptionElement.getText().equals(descricao)) {
                System.out.println("Portfólio com descrição '" +descricao +"' encontrado.");
                String precoUnitarioString = "$"+String.format(Locale.ENGLISH, "%.2f", valorTotal/quantidade);
                WebElement precoUnitarioElement = row.findElement(By.xpath(".//td[5]"));
                System.out.println("Preço unitário encontrado: " + precoUnitarioElement.getText());
                System.out.println("Preço unitário esperado: " + precoUnitarioString);
                assertEquals(precoUnitarioString, precoUnitarioElement.getText());
                return true;
            }
        }
        System.out.println("Portfólio com descrição '" +descricao +"' não encontrado.");
        return false;
    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
