package cn.ecosync.ibms.jpa;

import com.querydsl.jpa.codegen.JPADomainExporter;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.util.Properties;

public class JPADomainExporterRunnable implements Runnable {
    private final Properties projectProperties;

    public JPADomainExporterRunnable(Properties projectProperties) {
        this.projectProperties = projectProperties;
    }

    @Override
    public void run() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("querydsl");
            File targetFolder = new File(projectProperties.getProperty("basedir.ref") + "/target/generated-sources/java");
            JPADomainExporter exporter = new JPADomainExporter(targetFolder, emf.getMetamodel());
            exporter.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
