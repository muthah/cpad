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

        $j("#testbutton").click(function(){
            DWRAmrsReportService.testReportDownload(function(mapResult){
                alert("It is trying to download");
            });


        });

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
                        <input type="text" id="evaluationDate" name="${status.expression}" value="${status.value}"/>
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
                        Persistent ( <input type="text" size="4" /> or more ) CD4 counts less than <input type="text" size="4" /> , <input type="text" size="4" /> months or more after initiation of HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="testbutton" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        CD4 count drop to or below pre-treatment baseline level,  <input type="text" size="4" /> months or more after initiation of HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        CD4 count drop of  <input type="text" size="4" /> % (or more from on-treatment peak value during the follow-up period. Subsequent CD4 counts (after drop) failed to reach peak level ever attained on HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <label><b>ART Eligibility</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All patients with CD4 <  <input type="text" size="4" /> ml (irrespective of pregnancy) not on ART &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All TB/HIV co-infected patients not on ART &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <label><b>CD4 Testing</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All patients whose latest CD4 was done more than <input type="text" size="4" /> months ago &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
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
                        Children aged <input type="text" size="4" /> - <input type="text" size="4" /> months with absolute CD4 counts of <input type="text" size="4" /> (or below), <input type="text" size="4" /> months or more after initiation of HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                       Children older than  <input type="text" size="4" /> years with CD4 counts of <input type="text" size="4" /> (or below), <input type="text" size="4" /> months or more after initiation of HAART    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />                </td>
                </tr>
                <tr>
                    <td colspan="2">
                        CD4 count drop to or below pre-treatment baseline level,  <input type="text" size="4" /> months or more after initiation of HAART   &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        CD4 count drop of <input type="text" size="4" /> % (or more) from on-treatment peak value during the follow-up period. Subsequent CD4 counts (after drop) failed to reach peak level ever attained on HAART  &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <label><b>ART Eligibility</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All children less than  <input type="text" size="4" /> months regardless of CD4 counts, who are not on HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All children aged between  <input type="text" size="4" /> - <input type="text" size="4" /> months with CD4 counts less than <input type="text" size="4" /> , who are not on HAART &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All children aged between  <input type="text" size="4" /> - <input type="text" size="4" /> years with CD4 counts below <input type="text" size="4" /> , who are not on HAART
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <label><b>CD4 Testing</b></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        All patients whose latest CD4 was done more than <input type="text" size="4" /> months ago  &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="  +  " />
                    </td>
                </tr>


            </table>
        </fieldset>







    </form>

</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
