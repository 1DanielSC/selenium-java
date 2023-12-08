package com.example;

import java.util.List;

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
    public void cadastrarNovaConta(){
        webDriver.get(url);
        
        realizarLogin();
        sleep(2000);

        // Mover mouse para abrir menu lateral
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webDriver.findElement(By.xpath("//*[@id=\"sidenav-main\"]/div")));
        
        abrirSecaoCadastroDeConta();
        
        WebElement novaContaBtn = webDriver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/a"));
        novaContaBtn.click();
        
        WebElement corretoras = webDriver.findElement(By.xpath("//*[@id=\"account\"]/div[1]/div/div[1]/div/div/div/input"));
        corretoras.click();

        sleep(1500);
        preencherFormulario();
        sleep(3000);
        
        webDriver.quit();
    }
    
    private void realizarLogin(){
        WebElement emailElement = webDriver.findElement(By.xpath("//*[@id=\"login\"]/div[1]/div/input"));
        emailElement.sendKeys(loginEmail);
        WebElement passwordElement = webDriver.findElement(By.xpath("//*[@id=\"login\"]/div[2]/div/input"));
        passwordElement.sendKeys(loginPassword);
        WebElement loginBtn = webDriver.findElement(By.xpath("//*[@id=\"login\"]/div[3]/div[2]/button"));
        loginBtn.submit();
    }

    private void abrirSecaoCadastroDeConta(){
        sleep(2000);
        WebElement cadastrarMenuLateral = webDriver.findElement(By.xpath("//*[@id=\"sidenav-collapse-main\"]/ul/li[2]/a"));
        cadastrarMenuLateral.click();
        sleep(2000);
        WebElement cadastrarConta = webDriver.findElement(By.xpath("//*[@id=\"navbar-cadastro\"]/ul/li[1]/a"));
        cadastrarConta.click();
    }

    private void preencherFormulario(){
        WebElement dropdown=webDriver.findElement(By.xpath("//*[@id=\"leftMenu\"]/div[3]/div[1]/div[1]/ul"));
        List<WebElement> dropdownItens = dropdown.findElements(By.xpath(".//li"));
        
        for (WebElement item : dropdownItens) {
            if(item.getText().trim().equals("Tastyworks")){
                item.click();
            }
        }
        
        WebElement nome = webDriver.findElement(By.xpath("//*[@id=\"name\"]"));
        nome.sendKeys("Daniel Sehn Colao");
        
        WebElement numConta = webDriver.findElement(By.xpath("//*[@id=\"account_number\"]"));
        numConta.sendKeys("134456");
        
        WebElement alavancagem = webDriver.findElement(By.xpath("//*[@id=\"leverage\"]"));
        alavancagem.sendKeys("1");

        WebElement posicao = webDriver.findElement(By.xpath("//*[@id=\"account\"]/div[1]/div/div[4]/div/input"));
        posicao.sendKeys("1.5");
        
        WebElement submitBtn = webDriver.findElement(By.xpath("//*[@id=\"account\"]/div[2]/div/div/button"));
        submitBtn.click();
    }

    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
