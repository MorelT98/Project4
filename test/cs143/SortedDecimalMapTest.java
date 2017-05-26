package cs143;

import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author morel
 */
public class SortedDecimalMapTest {

    SortedDecimalMap<Product> tree;

    public SortedDecimalMapTest() {
    }

    @Before
    public void setUp() {
        tree = new SortedDecimalMap<>(3);
    }

    /**
     * Test of contains method, of class SortedDecimalMap.
     */
    
    @Test
    public void testContains() {
        assertFalse(tree.contains(125));
        tree.insert(new Product(555));
        assertTrue(tree.contains(555));
        assertFalse(tree.contains(123));
        assertFalse(tree.contains(1));
        tree.remove(555);
        assertFalse(tree.contains(555));
        tree.insert(new Product(321));
        assertFalse(tree.contains(320));
    }

    /**
     * Test of get method, of class SortedDecimalMap.
     */
    @Test
    public void testGet() {
        assertNull(tree.get(12));
        tree.insert(new Product(654, "Bread", 12, 65));
        assertEquals(tree.get(654).getProductName(), "Bread");
        assertNull(tree.get(0654));
        assertNull(tree.get(123));
        assertNull(tree.get(100));
        tree.insert(new Product(65));
        assertEquals(tree.get(65), new Product(65));
        tree.remove(654);
        assertNull(tree.get(650));
    }

    /**
     * Test of insert method, of class SortedDecimalMap.
     */
    @Test
    public void testInsert() {
        assertTrue(tree.insert(new Product(987)));
        assertTrue(tree.insert(new Product(21)));
        assertFalse(tree.insert(new Product(21)));
        assertTrue(tree.insert(new Product(215)));
        assertEquals(tree.get(21), new Product(21));
        assertEquals(tree.get(215), new Product(215));
        assertTrue(tree.insert(new Product(210)));
        tree.insert(new Product(984, "Vodka", 45, 13));
        Product product = tree.get(984);
        tree.remove(product.getProductId());
        product.setProductId(331);
        tree.insert(product);
        assertEquals(tree.get(331).getIsle(), 45);
        assertEquals(tree.get(331).getShelf(), 13);
        assertEquals(tree.get(331).hashCode(), 726);
        assertEquals(tree.get(331).toString(), "331");
        product = tree.get(331);
        product.setIsle(12);
        product.setShelf(1);
        product.setProductName("Beer");
        assertEquals(tree.get(331).getProductName(), "Beer");
        assertTrue(tree.get(331).equals(product));
        assertFalse(tree.get(331).equals(tree.get(330)));
        assertFalse(tree.get(331).equals("331"));
        assertFalse(tree.get(331).equals(new Product(330)));
        
    }

    /**
     * Test of remove method, of class SortedDecimalMap.
     */
    @Test
    public void testRemove() {
        assertFalse(tree.remove(250));
        tree.insert(new Product(234));
        assertTrue(tree.remove(234));
        assertFalse(tree.remove(234));
        assertTrue(tree.isEmpty());
        assertFalse(tree.remove(123));
        tree.insert(new Product(12));
        assertTrue(tree.remove(12));
        tree.insert(new Product(132));
        assertFalse(tree.remove(130));
    }

    /**
     * Test of isEmpty method, of class SortedDecimalMap.
     */
    @Test
    public void testIsEmpty() {
        assertTrue(tree.isEmpty());
        tree.insert(new Product(321));
        assertFalse(tree.isEmpty());
        tree.insert(new Product(322));
        assertFalse(tree.isEmpty());
    }

    /**
     * Test of iterator method, of class SortedDecimalMap.
     */
    @Test
    public void testIterator() {
        Iterator<Product> itr = tree.iterator();
        assertFalse(itr.hasNext());
        tree.insert(new Product(123));
        itr = tree.iterator();
        assertTrue(itr.hasNext());
        tree.insert(new Product(245));
        tree.insert(new Product(987));
        itr = tree.iterator();
        assertEquals(itr.next(), new Product(123));
        assertEquals(itr.next(), new Product(245));
        assertEquals(itr.next(), new Product(987));
        assertEquals(itr.next(), null);
        itr = tree.iterator();
        itr.next();
        itr.remove();
        assertEquals(itr.next(), new Product(245));
        itr.remove();
        itr.next();
        itr.remove();
        assertNull(itr.next());
        assertTrue(tree.isEmpty());
    }

}
