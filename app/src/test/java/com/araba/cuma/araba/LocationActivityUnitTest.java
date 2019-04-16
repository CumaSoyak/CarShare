package com.araba.cuma.araba;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocationActivityUnitTest {
    private Islem_sinifi islemsinifi = new Islem_sinifi();

    public LocationActivityUnitTest() {

    }
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    @Test
    public void karesini_al() throws Exception {
       assertEquals(9, islemsinifi.kare_al(3));

    }
    @Test
    public void asal_mi() throws Exception {
      Assert.assertEquals(true, islemsinifi.asal_sayi_bul(45));

    }


}