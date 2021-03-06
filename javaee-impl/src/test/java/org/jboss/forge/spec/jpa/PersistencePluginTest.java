package org.jboss.forge.spec.jpa;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

import java.util.List;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.project.Project;
import org.jboss.forge.spec.javaee.PersistenceFacet;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.PersistenceDescriptor;
import org.jboss.shrinkwrap.descriptor.api.spec.jpa.persistence.PersistenceUnitDef;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class PersistencePluginTest extends AbstractJPATest
{

   @Test
   public void testNewEntity() throws Exception
   {
      Project project = getProject();

      queueInputLines("");
      getShell().execute(
               "persistence setup --provider HIBERNATE --container CUSTOM_JTA --jndiDataSource java:jboss:jta-ds ");

      PersistenceDescriptor config = project.getFacet(PersistenceFacet.class).getConfig();
      List<PersistenceUnitDef> units = config.listUnits();
      PersistenceUnitDef unit = units.get(0);

      Assert.assertEquals("java:jboss:jta-ds", unit.getJtaDataSource());
   }

   @Test
   public void testAS6DataSource() throws Exception
   {
      Project project = getProject();

      queueInputLines("");
      getShell().execute(
               "persistence setup --provider HIBERNATE --container JBOSS_AS7");

      PersistenceDescriptor config = project.getFacet(PersistenceFacet.class).getConfig();
      List<PersistenceUnitDef> units = config.listUnits();
      PersistenceUnitDef unit = units.get(0);

      Assert.assertEquals("java:jboss/datasources/ExampleDS", unit.getJtaDataSource());
   }

   @Test
   public void testAS7DataSource() throws Exception
   {
      Project project = getProject();

      queueInputLines("");
      getShell().execute(
               "persistence setup --provider HIBERNATE --container JBOSS_AS7");

      PersistenceDescriptor config = project.getFacet(PersistenceFacet.class).getConfig();
      List<PersistenceUnitDef> units = config.listUnits();
      PersistenceUnitDef unit = units.get(0);

      Assert.assertEquals("java:jboss/datasources/ExampleDS", unit.getJtaDataSource());
   }

   @Test
   public void testHibernateProperties() throws Exception
   {
      Project project = getProject();

      queueInputLines("");
      getShell().execute(
               "persistence setup --provider HIBERNATE --container JBOSS_AS7");

      PersistenceDescriptor config = project.getFacet(PersistenceFacet.class).getConfig();
      List<PersistenceUnitDef> units = config.listUnits();
      PersistenceUnitDef unit = units.get(0);

      Assert.assertEquals("hibernate.hbm2ddl.auto", unit.getProperties().get(0).getName());
      Assert.assertEquals("create-drop", unit.getProperties().get(0).getValue());

      Assert.assertEquals("hibernate.show_sql", unit.getProperties().get(1).getName());
      Assert.assertEquals("true", unit.getProperties().get(1).getValue());

      Assert.assertEquals("hibernate.format_sql", unit.getProperties().get(2).getName());
      Assert.assertEquals("true", unit.getProperties().get(2).getValue());

      Assert.assertEquals("hibernate.transaction.flush_before_completion", unit.getProperties().get(3).getName());
      Assert.assertEquals("true", unit.getProperties().get(3).getValue());

      Assert.assertEquals(4, unit.getProperties().size());
   }

   @Test
   public void testMySQLDatabase() throws Exception
   {
      Project project = getProject();

      queueInputLines("");
      getShell().execute(
               "persistence setup --provider HIBERNATE --container JBOSS_AS7 --database MYSQL");

      PersistenceDescriptor config = project.getFacet(PersistenceFacet.class).getConfig();
      List<PersistenceUnitDef> units = config.listUnits();
      PersistenceUnitDef unit = units.get(0);

      Assert.assertEquals("hibernate.dialect", unit.getProperties().get(4).getName());
      Assert.assertEquals("org.hibernate.dialect.MySQLDialect", unit.getProperties().get(4).getValue());

      Assert.assertEquals(5, unit.getProperties().size());
   }

   @Test
   public void testMySQLDatabaseWithJndiDataSource() throws Exception
   {
      Project project = getProject();

      queueInputLines("");
      getShell()
               .execute(
                        "persistence setup --provider HIBERNATE --container JBOSS_AS6 --database MYSQL_INNODB --jndiDataSource java:demo");

      PersistenceDescriptor config = project.getFacet(PersistenceFacet.class).getConfig();

      List<PersistenceUnitDef> units = config.listUnits();
      PersistenceUnitDef unit = units.get(0);

      Assert.assertEquals("java:demo", unit.getJtaDataSource());
      Assert.assertEquals("hibernate.dialect", unit.getProperties().get(4).getName());
      Assert.assertEquals("org.hibernate.dialect.MySQLInnoDBDialect", unit.getProperties().get(4).getValue());

      Assert.assertEquals(5, unit.getProperties().size());
   }
}
