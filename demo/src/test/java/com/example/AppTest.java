package com.example;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * Unit test for simple App.
 */
public class AppTest {

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
    public void cadastrarContaComSuceso(){
        String corretora = "Tastyworks";
        String usuario = "Daniel Sehn Colao";

        // Gerar numero de conta
        int maxLength = 15;
        String numeroConta = UUID.randomUUID().toString().substring(0, maxLength);

        String alavancagem = "1";
        String posicao = "1.5";
        realizarCadastroDeConta(corretora, usuario, numeroConta, alavancagem, posicao);

        // Verificar se a conta está presente na lista de contas
        assertTrue(validarCadastroConta(numeroConta));
        webDriver.quit();
    }

    @Test
    public void cadastrarContaNomeInvalido(){
        String corretora = "Tastyworks";
        String usuario = "Daniel Sehn Colao";
        usuario = usuario.repeat(40);

        // Gerar numero de conta
        int maxLength = 15;
        String numeroConta = UUID.randomUUID().toString().substring(0, maxLength);

        String alavancagem = "1";
        String posicao = "1.5";
        realizarCadastroDeConta(corretora, usuario, numeroConta, alavancagem, posicao);

        // Verificar se a conta não está presente na lista de contas
        assertFalse(validarCadastroConta(numeroConta));
        webDriver.quit();
    }

    private void realizarCadastroDeConta(String corretora, String usuario, String numeroConta, String alavancagem, String posicao){
        webDriver.get(url);
        
        // Realizar login no sistema
        realizarLogin();
        sleep(2000);

        // Mover mouse para abrir menu lateral
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"sidenav-main\"]/div")));
        
        // Abrir seção de cadastro de conta
        abrirSecaoCadastroDeConta();
        
        sleep(1000);

        // Clicar no botão "Adicionar novo"
        WebElement novaContaBtn = webDriver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/a"));
        novaContaBtn.click();
        sleep(1000);
        preencherFormularioCadastro(corretora, usuario, numeroConta, alavancagem, posicao);
        sleep(3000);
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

    private void abrirSecaoCadastroDeConta(){
        // Clicar no botão "Cadastro" - Menu Dropdown
        WebElement cadastrarMenuLateral = webDriver.findElement(By.xpath("//*[@id=\"sidenav-collapse-main\"]/ul/li[2]/a"));
        cadastrarMenuLateral.click();

        // Clicar no botão "Contas"
        WebElement cadastrarConta = webDriver.findElement(By.xpath("//*[@id=\"navbar-cadastro\"]/ul/li[1]/a"));
        cadastrarConta.click();
    }

    private void preencherFormularioCadastro(String corretora, String usuario, String numeroConta, String alavancagem, String posicao){
        // Abrir listagem de corretoras
        WebElement corretoras = webDriver.findElement(By.xpath("//*[@id=\"account\"]/div[1]/div/div[1]/div/div/div/input"));
        corretoras.click();

        // Abrir listagem de corretoras disponíveis
        WebElement dropdown=webDriver.findElement(By.xpath("//*[@id=\"leftMenu\"]/div[3]/div[1]/div[1]/ul"));
        List<WebElement> dropdownItens = dropdown.findElements(By.xpath(".//li"));
        
        // Selecionar Corretora
        for (WebElement item : dropdownItens) {
            if(item.getText().trim().equals(corretora)){
                item.click();
            }
        }
        
        // Digitar nome de usuário
        WebElement nomeElmnt = webDriver.findElement(By.xpath("//*[@id=\"name\"]"));
        nomeElmnt.sendKeys(usuario);

        // Digitar numero da conta
        WebElement numContaElmnt = webDriver.findElement(By.xpath("//*[@id=\"account_number\"]"));
        numContaElmnt.sendKeys(numeroConta);

        // Digitar alavancagem
        WebElement alavancagemElmnt = webDriver.findElement(By.xpath("//*[@id=\"leverage\"]"));
        alavancagemElmnt.sendKeys(alavancagem);

        //  Digitar posicao
        WebElement posicaoElmnt = webDriver.findElement(By.xpath("//*[@id=\"account\"]/div[1]/div/div[4]/div/input"));
        posicaoElmnt.sendKeys(posicao);

        // Clicar no botão de confirmar cadastro
        WebElement submitBtn = webDriver.findElement(By.xpath("//*[@id=\"account\"]/div[2]/div/div/button"));
        submitBtn.click();
    }
    
    private boolean validarCadastroConta(String numeroConta){
        sleep(1500);
        
        try {
            WebElement cancelarBtn = webDriver.findElement(By.xpath("//*[@id=\"account\"]/div[2]/div/div/a"));
            cancelarBtn.click();
        } catch (Exception e) {
            // TODO: handle exception
        }
        WebElement table = webDriver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/table/tbody"));
        List<WebElement> tableRows = table.findElements(By.xpath(".//tr"));
        for (WebElement row : tableRows) {
            List<WebElement> rowElements = row.findElements(By.xpath(".//td"));
            for (WebElement data : rowElements) {
                System.out.println(data.getText());
                if(data.getText().equals(numeroConta)){
                    System.out.println("Conta com numero '" +numeroConta +"'' encontrada.");
                    return true;
                }
            }
        }
        System.out.println("Conta com numero '" +numeroConta +"'' não encontrada.");
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
