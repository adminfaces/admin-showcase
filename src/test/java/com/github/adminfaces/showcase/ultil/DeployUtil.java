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
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.github.adminfaces:admin-theme:jar:no-cache:?").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.github.adminfaces:admin-template").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces.extensions:primefaces-extensions").withTransitivity().asFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.omnifaces:omnifaces").withTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("net.bull.javamelody:javamelody-core").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("javax.json:javax.json-api").withTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.glassfish:javax.json").withTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("commons-io:commons-io").withTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.slf4j:slf4j-api").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.google.apis:google-api-services-drive").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.google.api-client:google-api-client:1.22.0").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.google.oauth-client:google-oauth-client:1.22.0").withTransitivity().asFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.google.http-client:google-http-client-jackson2:1.22.0").withTransitivity().asFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.commons:commons-compress").withTransitivity().asFile());


        //WEB-INF

        war.addAsWebInfResource(new File(WEB_INF, "beans.xml"), "beans.xml");
        war.addAsWebInfResource(new File(WEB_INF, "web.xml"), "web.xml");
        war.addAsWebInfResource(new File(WEB_INF, "faces-config.xml"), "faces-config.xml");
        war.addAsDirectory("sources");

        //resources
        war.addAsResource(new File(RESOURCES, "admin-config.properties"), "admin-config.properties");
        war.addAsResource(new File(RESOURCES, "showcase.properties"), "showcase.properties");
        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory("src/main/webapp").as(GenericArchive.class), "/", Filters.include(".*\\.(xhtml|html|css|js|png|gif|jpg|ico)$"));
        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory("src/main/java/com/github/adminfaces/showcase").as(GenericArchive.class), "/sources/com/github/adminfaces/showcase", Filters.include(".*\\.java$"));
        System.out.println(war.toString(true));
        return war;
    }
}
