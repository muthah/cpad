package org.openmrs.module.amrsreports.extension;

import java.util.LinkedHashMap;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.web.extension.AdministrationSectionExt;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.PrivilegeConstants;

/**
 * admin page extension
 */
public class AmrsReportAdminExt extends AdministrationSectionExt {

    /**
     * Defines the privilege required to the see the Administration section
     * for the module
     */


    /**
     *
     */
    @Override
    public Extension.MEDIA_TYPE getMediaType() {
        return Extension.MEDIA_TYPE.html;
    }

    /**
     * @see org.openmrs.module.web.extension.AdministrationSectionExt#getLinks()
     */
    @Override
    public Map<String, String> getLinks() {
        Map<String, String> map = new LinkedHashMap<String, String>();

        map.put("module/amrsreports/queuedReport.list", "Manage ICAP Reports");
        map.put("module/amrsreports/facility.list", "Manage ICAP Facilities");

        return map;
    }

    /**
     * @see org.openmrs.module.web.extension.AdministrationSectionExt#getTitle()
     */
    @Override
    public String getTitle() {
        return "ICAP  Reports Module";
    }
}
