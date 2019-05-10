// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.domain.test;

import org.phoenixctms.ctsms.domain.DaoTransformTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see org.phoenixctms.ctsms.domain.Hyperlink
*/
@Test(groups = { "transform", "HyperlinkDao" })
public class HyperlinkDaoTransformTest extends DaoTransformTestBase {

	/**
	 * Test for method HyperlinkDao.toHyperlinkInVO
	 *
	 * @see org.phoenixctms.ctsms.domain.HyperlinkDao#toHyperlinkInVO(org.phoenixctms.ctsms.domain.Hyperlink source, org.phoenixctms.ctsms.vo.HyperlinkInVO target)
	 */
	@Test
	public void testToHyperlinkInVO() {
		Assert.fail("Test 'HyperlinkDaoTransformTest.testToHyperlinkInVO' not implemented!");
	}

	/**
	* Test for method hyperlinkInVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.HyperlinkDao#hyperlinkInVOToEntity(org.phoenixctms.ctsms.vo.HyperlinkInVO source, org.phoenixctms.ctsms.domain.Hyperlink target, boolean copyIfNull)
	*/
	@Test
	public void testHyperlinkInVOToEntity() {
		Assert.fail("Test 'HyperlinkDaoTransformTest.testHyperlinkInVOToEntity' not implemented!");
	}

	/**
	 * Test for method HyperlinkDao.toHyperlinkOutVO
	 *
	 * @see org.phoenixctms.ctsms.domain.HyperlinkDao#toHyperlinkOutVO(org.phoenixctms.ctsms.domain.Hyperlink source, org.phoenixctms.ctsms.vo.HyperlinkOutVO target)
	 */
	@Test
	public void testToHyperlinkOutVO() {
		Assert.fail("Test 'HyperlinkDaoTransformTest.testToHyperlinkOutVO' not implemented!");
	}

	/**
	* Test for method hyperlinkOutVOToEntity
	*
	* @see org.phoenixctms.ctsms.domain.HyperlinkDao#hyperlinkOutVOToEntity(org.phoenixctms.ctsms.vo.HyperlinkOutVO source, org.phoenixctms.ctsms.domain.Hyperlink target, boolean copyIfNull)
	*/
	@Test
	public void testHyperlinkOutVOToEntity() {
		Assert.fail("Test 'HyperlinkDaoTransformTest.testHyperlinkOutVOToEntity' not implemented!");
	}
}