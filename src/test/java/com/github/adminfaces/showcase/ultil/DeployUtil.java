package com.github.adminfaces.showcase.ultil;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;

import java.io.File;

/**
 * Created by rafael-pestano on 16/01/17.
 */
public class DeployUtil {

    protected static final String RESOURCES = "src/main/resources";
    protected static final String WEB_INF = "src/main/webapp/WEB-INF";

    public static WebArchive deploy() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "showcase-test.war");
        war.addPackages(true, "com.github.adminfaces.showcase");
        //LIBS
        MavenResolverSystem resolver = Maven.resolver();
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.github.adminfaces:admin-theme").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.github.adminfaces:admin-template").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces.extensions:primefaces-extensions").withTransitivity().asFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.omnifaces:omnifaces").withTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("javax.json:javax.json-api").withTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.glassfish:javax.json").withTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("commons-io:commons-io").withTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.slf4j:slf4j-api").withoutTransitivity().asSingleFile());

        //WEB-INF

        war.addAsWebInfResource(new File(WEB_INF, "beans.xml"), "beans.xml");
        war.addAsWebInfResource(new File(WEB_INF, "web.xml"), "web.xml");
        war.addAsWebInfResource("test-faces-config.xml", "faces-config.xml");
        war.addAsDirectory("sources");

        //resources
        war.addAsResource(new File(RESOURCES, "admin-config.properties"), "admin-config.properties");

        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory("src/main/webapp").as(GenericArchive.class), "/", Filters.include(".*\\.(xhtml|html|css|js|png|gif|jpg|ico)$"));
        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory("src/main/java/com/github/adminfaces/showcase").as(GenericArchive.class), "/sources/com/github/adminfaces/showcase", Filters.include(".*\\.java$"));
        System.out.println(war.toString(true));
        return war;
    }
}
