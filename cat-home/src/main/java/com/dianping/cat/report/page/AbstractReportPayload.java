package com.dianping.cat.report.page;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dianping.cat.report.ReportPage;
import com.dianping.cat.report.page.model.spi.ModelPeriod;
import com.site.web.mvc.Action;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.payload.annotation.FieldMeta;

public abstract class AbstractReportPayload<A extends Action> implements ActionPayload<ReportPage, A> {
	protected static final long ONE_HOUR = 3600 * 1000L;

	private ReportPage m_defaultPage;

	private ReportPage m_page;

	@FieldMeta("ip")
	private String m_ipAddress;
	
	@FieldMeta("domain")
	private String m_domain;

	@FieldMeta("date")
	private long m_date;

	@FieldMeta("hours")
	private int m_hours;

	private SimpleDateFormat m_dateFormat = new SimpleDateFormat("yyyyMMddHH");

	public AbstractReportPayload(ReportPage defaultPage) {
		m_defaultPage = defaultPage;
	}

	public long getCurrentDate() {
		long timestamp = System.currentTimeMillis();

		return timestamp - timestamp % ONE_HOUR;
	}

	public long getDate() {
		long current = getCurrentDate();
		
		if (m_date <= 0) {
			return current + m_hours * ONE_HOUR;
		} else {
			long result = m_date + m_hours * ONE_HOUR;
			
			if (result > current) {
				return current;
			}
			return result;
		}
	}

	public long getRealDate() {
		return m_date;
	}

	public String getDomain() {
		return m_domain;
	}

	public int getHours() {
		return m_hours;
	}

	@Override
	public ReportPage getPage() {
		return m_page;
	}

	public ModelPeriod getPeriod() {
		return ModelPeriod.getByTime(getDate());
	}

	public void setDate(String date) {
		if (date == null || date.length() == 0) {
			m_date = getCurrentDate();
		} else {
			try {
				Date temp = m_dateFormat.parse(date);

				m_date = temp.getTime();
			} catch (Exception e) {
				// ignore it
				m_date = getCurrentDate();
			}
		}
	}

	public void setDomain(String domain) {
		m_domain = domain;
	}

	public void setHours(int hours) {
		m_hours = hours;
	}

	public void setPage(ReportPage page) {
		m_page = page;
	}

	@Override
	public void setPage(String page) {
		m_page = ReportPage.getByName(page, m_defaultPage);
	}

	public String getIpAddress() {
   	return m_ipAddress;
   }

	public void setIpAddress(String ipAddress) {
   	m_ipAddress = ipAddress;
   }
}
