<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>


<openmrs:require privilege="Run Reports" otherwise="/login.htm" redirect="/module/amrsreports/parametizedReport.form"/>

<%@ include file="localHeader.jsp" %>


<c:if test="${not empty queuedReports.queuedReportId}">
    <h2>Edit Patient Management Report</h2>
</c:if>

<c:if test="${empty queuedReports.queuedReportId}">
    <h2>Add Patient Management Report</h2>
</c:if>

<style>
    fieldset.visualPadding {
        padding: 1em;
    }

    .right {
        text-align: right;
    }

    input.hasDatepicker {
        width: 14em;
    }
</style>

<script type="text/javascript">

    var reportDate;
    var scheduleDate;
    var evaluationEndDate;

    $j(document).ready(function () {

        reportDate = new DatePicker("<openmrs:datePattern/>", "evaluationDate", {});
        reportDate.setDate(new Date());

        scheduleDate = new DatePicker("<openmrs:datePattern/>", "dateScheduled", {});
        scheduleDate.setDate(new Date());

        evaluationEndDate = new DatePicker("<openmrs:datePattern/>", "reportingEndDate", { });
        evaluationEndDate.setDate(new Date());



        $j("#adultcd4report").click(function(){
            var startDate = String($j("#evaluationDate").val());
            var maxMonths = $j("#adultsMaxMonths").val();

            DWRAmrsReportService.saveAdultsCd4DoneMoreThan6Months(startDate,maxMonths,function(mapResult){
                alert("It is trying to download");
            });


        });

       /* $j("#testbutton").click(function(){
            DWRAmrsReportService.testReportDownload(function(mapResult){
                alert("It is trying to download");
            });


        });*/

    });

</script>


<b class="boxHeader">Scheduled Report Details</b>

<div class="box" style=" width:99%; height:auto;  overflow-x: auto;">

    <spring:hasBindErrors name="queuedReports">
        <spring:message code="fix.error"/>
        <br/>
    </spring:hasBindErrors>

    <form method="POST">
        <fieldset class="visualPadding">
            <table cellpadding="2" cellspacing="0">
                <tr>
                    <td>The patient who made any visit between this date up to today is considered as Active Patient</td>
                    <td>
                        <spring:bind path="queuedReports.evaluationDate">
                        <input type="text" id="evaluationDate" name="evaluationDate" value="${status.value}"/>
                        <c:if test="${status.error}">
                            Error codes:
                            <c:forEach items="${status.errorMessages}" var="error">
                                <c:out value="${error}"/>
                            </c:forEach>
                        </c:if>
                    </spring:bind>
                    </td>
                </tr>
            </table>
            <legend>Adults and Adolescents</legend>
            <table cellspacing="0" cellpadding="2">
                <tr>
                    <td colspan="2">
                        <label><b>Treatment Failure</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        Persistent ( <input type="text" name="adultPersistencetimes" id="adultPersistencetimes" size="4" /> or more ) CD4 counts less than <input type="text" name="adt_maxcd4" id="adt_maxcd4" size="4" /> , <input type="text" name="adt_noOfMonths" id="adt_noOfMonths" size="4" /> months or more after initiation of HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="adultsPersistence" id="adultsPersistence" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        CD4 count drop to or below pre-treatment baseline level,  <input type="text" size="4" name="adult_cd4_below_pretreatment_no_of_months" /> months or more after initiation of HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="adultCd4DropBelowPreTreatmentReport" id="adultCd4DropBelowPreTreatmentReport" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        CD4 count drop of  <input type="text" name="adult_perc_cd4drop_param" size="4" /> % (or more from on-treatment peak value during the follow-up period. Subsequent CD4 counts (after drop) failed to reach peak level ever attained on HAART &nbsp;&nbsp;&nbsp;&nbsp;<input name="adultsCd4DropPercentage" id="adultsCd4DropPercentage" type="submit" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <label><b>ART Eligibility</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All patients with CD4 <  <input type="text" name="adultsMaxCD4noPreg" size="4" /> ml (irrespective of pregnancy) not on ART &nbsp;&nbsp;&nbsp;&nbsp;<input id="adultPatientsNotOnART" name="adultPatientsNotOnART" type="submit" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All TB/HIV co-infected patients not on ART &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="adultsTBHIVCoinfectedReport" id="adultsTBHIVCoinfectedReport" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <label><b>CD4 Testing</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All patients whose latest CD4 was done more than <input type="text" name="adultsLatestCD4" size="4" /> months ago &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="adultsLatestCD4Report"   value="  +  " />
                    </td>
                </tr>


            </table>
        </fieldset>
        <fieldset class="visualPadding">

            <legend>Pediatric</legend>
            <table cellspacing="0" cellpadding="2">
                <tr>
                    <td colspan="2">
                        <label><b>Treatment Failure</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        Children aged <input type="text" size="4" name="ped_tf_rpt1_min_age"  /> - <input type="text" size="4" name="ped_tf_rpt1_max_age" /> months with absolute CD4 counts of <input type="text" size="4" name="ped_tf_rpt1_max_cd4" /> (or below), <input type="text" name="ped_tf_rpt1_months" size="4" /> months or more after initiation of HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="ped_tf_rpt1" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                       Children older than  <input type="text" size="4" name="ped_tf_rpt2_min_age" /> years with CD4 counts of <input type="text" size="4" name="ped_tf_rpt2_max_cd4" /> (or below), <input type="text" name="ped_tf_rpt2_months" size="4" /> months or more after initiation of HAART    &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="ped_tf_rpt2" value="  +  " />                </td>
                </tr>
                <tr>
                    <td colspan="2">
                        CD4 count drop to or below pre-treatment baseline level,  <input type="text" name="ped_tf_rpt3_months" size="4" /> months or more after initiation of HAART   &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="ped_tf_rpt3" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        CD4 count drop of <input type="text" name="ped_tf_rpt4_cd4_perc" size="4" /> % (or more) from on-treatment peak value during the follow-up period. Subsequent CD4 counts (after drop) failed to reach peak level ever attained on HAART  &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="ped_tf_rpt4" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <label><b>ART Eligibility</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All children less than  <input type="text" size="4" name="childrenNotInHaartAgeInM" /> months regardless of CD4 counts, who are not on HAART &nbsp;&nbsp;&nbsp;&nbsp;<input name="childrenNotInHaartReport" type="submit" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All children aged between  <input type="text" size="4" name="childrenMinAgeM" /> - <input type="text" size="4" name="childrenMaxAgeM" /> months with CD4 counts less than <input type="text" size="4" name="childrenMaxCD4M" /> , who are not on HAART &nbsp;&nbsp;&nbsp;&nbsp;<input name="childrenCD4MReport"  type="submit" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All children aged between  <input type="text" size="4" name="childrenMinAgeY" /> - <input name="childrenMaxAgeY" type="text" size="4" /> years with CD4 counts below <input type="text" size="4" name="childrenMaxCD4Y" /> , who are not on HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="childrenCD4YReport" value="  +  " />
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <label><b>CD4 Testing</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All patients whose latest CD4 was done more than <input type="text" name="childrenLatestCD4" size="4" /> months ago  &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="childrenLatestCD4Report" value="  +  " />
                    </td>
                </tr>


            </table>
        </fieldset>

    </form>

</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
