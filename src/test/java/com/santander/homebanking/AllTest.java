package com.santander.homebanking;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value= Suite.class)
@Suite.SuiteClasses({
        ClientServiceTest.class,
        AccountServiceTest.class
})
public class AllTest {
}
