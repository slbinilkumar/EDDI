package io.sls.core.parser.internal.matches;

import org.junit.Assert;
import org.junit.Test;

/**
 * Copyright by Spoken Language System. All rights reserved.
 * User: jarisch
 * Date: 02.11.12
 * Time: 14:32
 */
public class IterationCounterTest {

    /**
     * TODO IterationCounter does not get to 1-1-1 or 2-2-2
     * @throws Exception
     */
    @Test
    public void testCreateIterationPlan() throws Exception {
        //setup
        IterationCounter counter = new IterationCounter(3, new Integer[]{2, 2, 2});

        //assert
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{0, 0, 0}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{0, 0, 1}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{0, 1, 0}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{1, 0, 0}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{0, 0, 2}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{0, 2, 0}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{2, 0, 0}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{0, 1, 1}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{1, 0, 1}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{1, 1, 0}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{0, 1, 2}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{0, 2, 1}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{1, 0, 2}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{1, 2, 0}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{2, 0, 1}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{2, 1, 0}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{0, 2, 2}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{2, 0, 2}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{2, 2, 0}, counter.next().getIndexes());
        /*Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{1, 1, 1}, counter.next().getIndexes());*/
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{1, 1, 2}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{1, 2, 1}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{2, 1, 1}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{1, 2, 2}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{2, 1, 2}, counter.next().getIndexes());
        Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{2, 2, 1}, counter.next().getIndexes());
        /*Assert.assertTrue(counter.hasNext());
        Assert.assertArrayEquals(new Integer[]{2, 2, 2}, counter.next().getIndexes());*/
        Assert.assertFalse(counter.hasNext());

    }

    /*@Test
    public void testCreateIterationPlan1() throws Exception {
        //setup
        IterationCounter counter = new IterationCounter(3, new Integer[]{2, 2, 2});

        while (counter.hasNext()) {
            System.out.println(Arrays.toString(counter.next().getIndexes()));
        }
    }*/
}
