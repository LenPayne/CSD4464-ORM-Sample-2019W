/*
 * The MIT License
 *
 * Copyright 2019 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package csd4464ormsample2019w;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * A Sample of using the JPA in a Java EE Application Client Project
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Establish the Entity Manager based on the PU setup in the persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CSD4464-ORM-Sample-2019WPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        // Setup some Re-Used Variables
        List<ProductCode> pcList;
        ProductCode newPc;

        // Retrieve all ProductCodes using JPQL
        Query q = em.createQuery("SELECT pc FROM ProductCode pc");
        pcList = q.getResultList();
        showList("All Product Codes", pcList);

        // Retrieve one ProductCode using a Named Query
        q = em.createNamedQuery("ProductCode.findByProdCode");
        q.setParameter("prodCode", "SW");
        pcList = q.getResultList();
        showList("Product Codes with prodCode 'SW'", pcList);

        // Create a new ProductCode in Java
        newPc = new ProductCode();
        newPc.setProdCode("WW");
        newPc.setDiscountCode('M');
        newPc.setDescription("Wetware");

        // Begin a Transaction and Persist the new ProductCode to the DB
        et.begin();
        em.persist(newPc);
        et.commit();

        // Retrieve all ProductCodes using a Named Query
        pcList = em.createNamedQuery("ProductCode.findAll").getResultList();
        showList("All Product Codes after Persistence", pcList);

        // Update the Existing ProductCode
        newPc.setDiscountCode('L');

        // Begin a Transaction and Update the Existing ProductCode in the DB
        et.begin();
        em.merge(newPc);
        et.commit();

        // Retrieve a Specific ProductCode using a JPQL Query
        q = em.createQuery("SELECT pc FROM ProductCode pc WHERE pc.prodCode = :prodCode");
        q.setParameter("prodCode", "WW");
        pcList = q.getResultList();
        showList("WW Product Code after Merging", pcList);

        // Begin a Transaction to Remove the new ProductCode in the DB
        et.begin();
        em.remove(newPc);
        et.commit();

        // Retrieve all ProductCodes using a JPQL Query
        q = em.createQuery("SELECT pc FROM ProductCode pc");
        pcList = q.getResultList();
        showList("All Product Codes after Removal", pcList);

    }

    /**
     * Output to Standard Output a basic list of ProductCodes
     *
     * @param heading a message to start off the list
     * @param pcList a list of ProductCodes
     */
    private static void showList(String heading, List<ProductCode> pcList) {
        System.out.println("\n" + heading);
        for (ProductCode pc : pcList) {
            System.out.printf("%s:\t%c\t%s\n", pc.getProdCode(), pc.getDiscountCode(), pc.getDescription());
        }
    }

}
