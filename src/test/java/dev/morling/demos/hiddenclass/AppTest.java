package dev.morling.demos.hiddenclass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

import com.example.Customer;

public class AppTest {

    @Test
    public void testHiddenClass() throws Throwable {
        PropertyAcessor<Customer> accessor = PropertyAccessorFactory.getPropertyAccessor(Customer.class);

        Customer customer = new Customer("Duke", LocalDate.of(1995, Month.MAY, 23), "Main Street");
        assertEquals("Duke", accessor.getValue(customer, "name"));
        assertEquals(LocalDate.of(1995, Month.MAY, 23), accessor.getValue(customer, "birthday"));
        assertEquals("Main Street", accessor.getValue(customer, "address"));

        assertTrue(accessor.getClass().isHidden());
        assertNull(accessor.getClass().getCanonicalName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCannotLoadHiddenClass() throws Throwable {
        PropertyAcessor<Customer> accessor = PropertyAccessorFactory.getPropertyAccessor(Customer.class);
        accessor.getClass().getClassLoader().loadClass(accessor.getClass().getName());
    }
}
