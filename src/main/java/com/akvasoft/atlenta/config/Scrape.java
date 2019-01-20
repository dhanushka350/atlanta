package com.akvasoft.atlenta.config;

import com.akvasoft.atlenta.modal.Atlanta;
import com.akvasoft.atlenta.repo.repository;
import org.openqa.selenium.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scrape implements InitializingBean {

    Atlanta atlanta = null;
    WebDriver driver2 = null;

    @Autowired
    private repository repository;

    @Override
    public void afterPropertiesSet() {
        while (true) {

            try {

                WebDriver driver = new FireFox().getFirefoxDriver();
                driver2 = new FireFox().getFirefoxDriver();
                driver.get("https://aca3.accela.com/ATLANTA_GA/Cap/CapHome.aspx?module=Building&TabName=Building");
                JavascriptExecutor jse = (JavascriptExecutor) driver;
                WebElement search = driver.findElement(By.xpath("//*[@id=\"ctl00_PlaceHolderMain_btnNewSearch\"]"));
                jse.executeScript("scroll" + search.getLocation() + ";");
                search.click();


                while (true) {
                    Thread.sleep(15000);
                    WebElement table = driver.findElement(By.xpath("//*[@id=\"MainContent\"]"))
                            .findElement(By.id("ctl00_PlaceHolderMain_searchSection"))
                            .findElement(By.tagName("div"))
                            .findElement(By.tagName("div"))
                            .findElements(By.xpath("./*"))
                            .get(2)
                            .findElement(By.id("ctl00_PlaceHolderMain_updatePanel2"))
                            .findElements(By.xpath("./*"))
                            .get(2)
                            .findElements(By.xpath("./*"))
                            .get(0)
                            .findElement(By.className("ACA_Grid_OverFlow"))
                            .findElement(By.tagName("table"));


                    for (WebElement tr : table.findElement(By.tagName("tbody")).findElements(By.xpath("./*"))) {
                        if (tr.getAttribute("class").contains("ACA_TabRow_Header")) {
                            continue;
                        }

                        if (tr.getAttribute("class").contains("ACA_Table_Pages")) {
                            List<WebElement> pagination = tr.findElement(By.tagName("td"))
                                    .findElement(By.tagName("table"))
                                    .findElement(By.tagName("tbody"))
                                    .findElement(By.tagName("tr"))
                                    .findElements(By.xpath("./*"));

                            int size = pagination.size();
                            WebElement next = pagination.get(size - 1);
                            jse.executeScript("scroll" + next.getLocation() + ";");
                            try {
                                next.click();
                            } catch (ElementClickInterceptedException e) {
                                Thread.sleep(20000);
                                next.click();
                            }
                        }

                        if (tr.getAttribute("class").contains("ACA_TabRow_Odd") || tr.getAttribute("class").contains("ACA_TabRow_Even")) {
                            scrapeRow(tr);
                        }


                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean scrapeRow(WebElement tr) throws Exception {
        String date = "";
        String rec_no = "";
        String rec_type = "";
        String address = "";
        String desc = "";
        String permit = "";
        String status = "";
        String inner_page = "";
        String street_address = "";
        String rest = "";
        String city = "";
        String state = "";
        String zip = "";
        atlanta = new Atlanta();

        List<WebElement> tds = tr.findElements(By.xpath("./*"));
        date = tds.get(1).getAttribute("innerText");
        rec_no = tds.get(2).getAttribute("innerText");
        rec_type = tds.get(3).getAttribute("innerText");
        address = tds.get(4).getAttribute("innerText");
        desc = tds.get(5).getAttribute("innerText");
        permit = tds.get(6).getAttribute("innerText");
        status = tds.get(7).getAttribute("innerText");

        Atlanta isAdded = repository.findTopByDateEqualsAndRecordNo(date, rec_no);
        if (isAdded != null) {
            System.err.println("==================================");
            System.err.println("==============SKIPPED=============");
            System.err.println("==================================");
            return false;
        }
        try {
            inner_page = tds.get(2).findElement(By.tagName("div")).findElement(By.tagName("a")).getAttribute("href");
            System.out.println("DATE - " + date);
            System.out.println("REC_NO - " + rec_no);
            System.out.println("REC_TYPE - " + rec_type);
            System.out.println("ADDRESS - " + address);
            System.out.println("DESC - " + desc);
            System.out.println("PERMIT - " + permit);
            System.out.println("STATUS - " + status);
            System.out.println("INNER_PAGE - " + inner_page);

            int length = address.split(",").length;

            street_address = address.split(",")[0];
            rest = address.split(",")[length - 1];

            city = rest.split(" ")[1];
            state = rest.split(" ")[2];

            try {
                zip = rest.split(" ")[3];
            } catch (ArrayIndexOutOfBoundsException e) {
                zip = "--";
            }
            System.out.println("===================== CITY " + city);
            System.out.println("===================== STATE " + state);
            System.out.println("===================== ZIP " + zip);

            atlanta.setDate(date);
            atlanta.setRecord_no(rec_no);
            atlanta.setRecord_type(rec_type);
            atlanta.setAddress(street_address);
            atlanta.setCity(city);
            atlanta.setState(state);
            atlanta.setZip(zip);
            atlanta.setDescription(desc);
            atlanta.setPermit(permit);
            atlanta.setStatus(status);
            atlanta.setUrl(inner_page);
            scrapeInnerPage(atlanta);

        } catch (NoSuchElementException e) {
            atlanta.setDate(date);
            atlanta.setRecord_no(rec_no);
            atlanta.setRecord_type(rec_type);
            atlanta.setAddress(street_address);
            atlanta.setCity(city);
            atlanta.setState(state);
            atlanta.setZip(zip);
            atlanta.setDescription(desc);
            atlanta.setPermit(permit);
            atlanta.setStatus(status);
            atlanta.setUrl("--");
            repository.save(atlanta);
            System.err.println("A TAG IS NOT HERE...");
        }

        return true;
    }

    private void scrapeInnerPage(Atlanta atlanta) throws Exception {

        System.err.println("INNER PAGE - " + atlanta.getUrl());
        driver2.get(atlanta.getUrl());
        Thread.sleep(5000);
        WebElement details = driver2.findElement(By.xpath("//*[@id=\"tab-record_detail\"]"));


        String contact = details.findElement(By.id("ctl00_PlaceHolderMain_divWorkLocation"))
                .findElement(By.id("divWorkLocationInfo"))
                .findElement(By.id("divWorkLocationDetail")).getAttribute("innerText");

        WebElement record_details = details.findElement(By.id("ctl00_PlaceHolderMain_divPermitDetail"))
                .findElement(By.id("divPermitDetailInfo"))
                .findElement(By.id("ctl00_PlaceHolderMain_PermitDetailList1_updatePanel"))
                .findElements(By.xpath("./*"))
                .get(0)
                .findElement(By.tagName("table"))
                .findElement(By.tagName("tbody"));


        WebElement element = driver2.findElement(By.xpath("//*[@id=\"ctl00_PlaceHolderMain_dvContent\"]/div[1]/div[1]"));
        String residential = element.findElements(By.xpath("./*")).get(1).getAttribute("innerText");

        if (residential.contains("Residential")) {
            residential = residential.replace("Residential", "").replace("-", "");
            atlanta.setResidential(residential);
        }

        if (contact.length() < 1) {
            contact = "--";
        }
        System.err.println("CONTACT " + contact);

        atlanta.setContact(contact);

        try {
            WebElement applicant = record_details.findElements(By.xpath("./*")).get(0).findElements(By.xpath("./*")).get(0);
            System.err.println("APPLICANT " + applicant.findElement(By.tagName("div"))
                    .findElements(By.xpath("./*")).get(1)
                    .findElement(By.tagName("table"))
                    .findElement(By.tagName("tbody")).getAttribute("innerText"));

            atlanta.setApplicant(applicant.findElement(By.tagName("div"))
                    .findElements(By.xpath("./*")).get(1)
                    .findElement(By.tagName("table"))
                    .findElement(By.tagName("tbody")).getAttribute("innerText"));

        } catch (IndexOutOfBoundsException e) {
            atlanta.setApplicant("--");
            System.err.println("APPLICANT NOT FOUND");
        }
        System.out.println("============================================================");


        try {
            WebElement professional = record_details.findElements(By.xpath("./*")).get(0).findElements(By.xpath("./*")).get(1);
            System.err.println("PROFESSIONAL " + professional.findElement(By.tagName("div"))
                    .findElements(By.xpath("./*")).get(1)
                    .findElement(By.tagName("table"))
                    .findElement(By.tagName("tbody")).getAttribute("innerText"));

            atlanta.setProfessional(professional.findElement(By.tagName("div"))
                    .findElements(By.xpath("./*")).get(1)
                    .findElement(By.tagName("table"))
                    .findElement(By.tagName("tbody")).getAttribute("innerText"));


        } catch (IndexOutOfBoundsException e) {
            System.err.println("PROFESSIONAL NOT FOUND");
            atlanta.setProfessional("--");
        }
        System.out.println("============================================================");

        try {
            WebElement project_desc = record_details.findElements(By.xpath("./*")).get(1).findElements(By.xpath("./*")).get(0);
            System.err.println("PROJECT DESCRIPTION " + project_desc.findElement(By.tagName("div"))
                    .findElements(By.xpath("./*")).get(1)
                    .findElement(By.tagName("table"))
                    .findElement(By.tagName("tbody")).getAttribute("innerText"));

            atlanta.setProject_desc(project_desc.findElement(By.tagName("div"))
                    .findElements(By.xpath("./*")).get(1)
                    .findElement(By.tagName("table"))
                    .findElement(By.tagName("tbody")).getAttribute("innerText"));


        } catch (IndexOutOfBoundsException e) {
            System.err.println("PROJECT DESCRIPTION NOT FOUND");
            atlanta.setProject_desc("--");
        }
        System.out.println("============================================================");


        try {
            WebElement owner = record_details.findElements(By.xpath("./*")).get(1).findElements(By.xpath("./*")).get(1);

            System.err.println("OWNER " + owner.findElement(By.tagName("div"))
                    .findElements(By.xpath("./*")).get(1)
                    .findElement(By.tagName("table"))
                    .findElement(By.tagName("tbody")).getAttribute("innerText"));

            atlanta.setOwner(owner.findElement(By.tagName("div"))
                    .findElements(By.xpath("./*")).get(1)
                    .findElement(By.tagName("table"))
                    .findElement(By.tagName("tbody")).getAttribute("innerText"));
        } catch (IndexOutOfBoundsException e) {
            System.err.println("OWNER NOT FOUND");
            atlanta.setOwner("--");
        }


        repository.save(atlanta);
    }
}
