package org.phoenixctms.ctsms.compare;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import static java.util.Comparator.*;
import java.util.List;

import org.junit.Test;

public class ComparatorFactoryTest {

	private static class AandB {

		boolean isA;
		Integer num;
		String str;

		public AandB(boolean isA, Integer num, String str) {
			super();
			this.isA = isA;
			this.num = num;
			this.str = str;
		}
		
		public boolean isA() {
			return isA;
		}

		
		public String getStr() {
			return str;
		}

		
		public Integer getNum() {
			return num;
		}
	}

	@Test
	public void testDecide() {
		
		Comparator<AandB> deciderComparator = Comparators.decide(AandB::isA,comparing(AandB::getNum,naturalOrder()),comparing(AandB::getStr,naturalOrder()));
		
		List<AandB> as = Arrays.asList(new AandB(true, 3, null), new AandB(true, 1, null), new AandB(true, 2, null));
		as.sort(deciderComparator);
		assertEquals(1, as.get(0).getNum().intValue());
		assertEquals(2, as.get(1).getNum().intValue());
		assertEquals(3, as.get(2).getNum().intValue());
		
		List<AandB> bs = Arrays.asList(new AandB(false, null, "b"), new AandB(false, null, "c"), new AandB(false, null, "a"));
		bs.sort(deciderComparator);
		assertEquals("a", bs.get(0).getStr());
		assertEquals("b", bs.get(1).getStr());
		assertEquals("c", bs.get(2).getStr());
		
		List<AandB> mixed = Arrays.asList(new AandB(false, 3, "b"), new AandB(true, 1, "c"), new AandB(false, 2, "a"));
		mixed.sort(deciderComparator);
		assertEquals("c", mixed.get(0).getStr());
		assertEquals("a", mixed.get(1).getStr());
		assertEquals("b", mixed.get(2).getStr());
		
	}
}
