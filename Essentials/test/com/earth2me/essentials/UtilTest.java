package com.earth2me.essentials;

import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.Util;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.bukkit.World.Environment;
import org.bukkit.plugin.InvalidDescriptionException;


public class UtilTest extends EssentialsTest
{

	public UtilTest(String name)
	{
		super(name);
	}

	public void testFDDnow()
	{
		Calendar c = new GregorianCalendar();
		String resp = DateUtil.formatDateDiff(c, c);
		assertEquals("now", resp);
	}

	public void testFDDfuture()
	{
		Calendar a, b;
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 10, 0, 1);
		assertEquals(" 1 second", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 10, 0, 2);
		assertEquals(" 2 seconds", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 10, 0, 3);
		assertEquals(" 3 seconds", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 10, 1, 0);
		assertEquals(" 1 minute", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 10, 2, 0);
		assertEquals(" 2 minutes", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 10, 3, 0);
		assertEquals(" 3 minutes", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 11, 0, 0);
		assertEquals(" 1 hour", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 12, 0, 0);
		assertEquals(" 2 hours", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 13, 0, 0);
		assertEquals(" 3 hours", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 2, 10, 0, 0);
		assertEquals(" 1 day", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 3, 10, 0, 0);
		assertEquals(" 2 days", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 4, 10, 0, 0);
		assertEquals(" 3 days", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 2, 1, 10, 0, 0);
		assertEquals(" 1 month", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 3, 1, 10, 0, 0);
		assertEquals(" 2 months", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 4, 1, 10, 0, 0);
		assertEquals(" 3 months", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2011, 1, 1, 10, 0, 0);
		assertEquals(" 1 year", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2012, 1, 1, 10, 0, 0);
		assertEquals(" 2 years", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2013, 1, 1, 10, 0, 0);
		assertEquals(" 3 years", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2011, 4, 5, 23, 38, 12);
		assertEquals(" 1 year 3 months 4 days 13 hours 38 minutes 12 seconds", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 9, 17, 23, 45, 45);
		b = new GregorianCalendar(2015, 3, 7, 10, 0, 0);
		assertEquals(" 4 years 5 months 20 days 10 hours 14 minutes 15 seconds", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2011, 4, 31, 10, 0, 0);
		b = new GregorianCalendar(2011, 4, 31, 10, 5, 0);
		assertEquals(" 5 minutes", DateUtil.formatDateDiff(a, b));
	}

	public void testFDDpast()
	{
		Calendar a, b;
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 9, 59, 59);
		assertEquals(" 1 second", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 9, 59, 58);
		assertEquals(" 2 seconds", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 9, 59, 57);
		assertEquals(" 3 seconds", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 9, 59, 0);
		assertEquals(" 1 minute", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 9, 58, 0);
		assertEquals(" 2 minutes", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 9, 57, 0);
		assertEquals(" 3 minutes", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 9, 0, 0);
		assertEquals(" 1 hour", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 8, 0, 0);
		assertEquals(" 2 hours", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 1, 7, 0, 0);
		assertEquals(" 3 hours", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 5, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 4, 10, 0, 0);
		assertEquals(" 1 day", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 5, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 3, 10, 0, 0);
		assertEquals(" 2 days", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 5, 10, 0, 0);
		b = new GregorianCalendar(2010, 1, 2, 10, 0, 0);
		assertEquals(" 3 days", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 5, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 4, 1, 10, 0, 0);
		assertEquals(" 1 month", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 5, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 3, 1, 10, 0, 0);
		assertEquals(" 2 months", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 5, 1, 10, 0, 0);
		b = new GregorianCalendar(2010, 2, 1, 10, 0, 0);
		assertEquals(" 3 months", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2009, 1, 1, 10, 0, 0);
		assertEquals(" 1 year", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2008, 1, 1, 10, 0, 0);
		assertEquals(" 2 years", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2007, 1, 1, 10, 0, 0);
		assertEquals(" 3 years", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 1, 1, 10, 0, 0);
		b = new GregorianCalendar(2009, 4, 5, 23, 38, 12);
		assertEquals(" 8 months 26 days 10 hours 21 minutes 48 seconds", DateUtil.formatDateDiff(a, b));
		a = new GregorianCalendar(2010, 9, 17, 23, 45, 45);
		b = new GregorianCalendar(2000, 3, 7, 10, 0, 0);
		assertEquals(" 10 years 6 months 10 days 13 hours 45 minutes 45 seconds", DateUtil.formatDateDiff(a, b));
	}
	
	public void filenameTest() {
		try
		{
			assertEquals("_-", Util.sanitizeFileName("\u0000"));
			assertEquals("_-", Util.sanitizeFileName("\u0001"));
			assertEquals("_-", Util.sanitizeFileName("\u001f"));
			assertEquals(" -", Util.sanitizeFileName(" "));
			assertEquals("_-", Util.sanitizeFileName(".."));
			assertEquals("_-", Util.sanitizeFileName("..\\"));
			assertEquals("_-", Util.sanitizeFileName("../"));
			assertEquals("_-", Util.sanitizeFileName("\""));
			assertEquals("_-", Util.sanitizeFileName("<>?:*."));
			assertEquals("a-0fa", Util.sanitizeFileName("aä"));
			
		}
		catch (InvalidNameException ex)
		{
			Logger.getLogger(UtilTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
}
