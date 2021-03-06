<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>


<openmrs:require privilege="Run Reports" otherwise="/login.htm" redirect="/module/amrsreports/queuedReport.form"/>

<%@ include file="localHeader.jsp" %>


<c:if test="${not empty queuedReports.queuedReportId}">
    <h2>Edit Scheduled Report</h2>
</c:if>

<c:if test="${empty queuedReports.queuedReportId}">
    <h2>Add Scheduled Report</h2>
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



    });

    function checkFacility(){
        var indicator= $j("#facility").val();
        if(indicator ==null){
            alert("You must select one or more facility/location to proceed");

            return false;
        }
        return true;
    }

</script>


<b class="boxHeader">Scheduled Report Details</b>

<div class="box" style=" width:99%; height:auto;  overflow-x: auto;">

    <spring:hasBindErrors name="queuedReports">
        <spring:message code="fix.error"/>
        <br/>
    </spring:hasBindErrors>

    <form method="POST" onsubmit="return checkFacility();">
        <fieldset class="visualPadding">
            <legend>Dates</legend>
            <table cellspacing="0" cellpadding="2">
                <tr>
                    <td class="right">
                        <label for="evaluationDate">Report date (as of/from):</label>
                    </td>
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
                <tr>
                    <td class="right">
                        <label for="reportingEndDate">Report date (to):</label>
                    </td>
                    <td>
                        <spring:bind path="queuedReports.reportingEndDate">
                            <input type="text" id="reportingEndDate" name="${status.expression}" value="${status.value}"/>
                            <c:if test="${status.error}">
                                Error codes:
                                <c:forEach items="${status.errorMessages}" var="error">
                                    <c:out value="${error}"/>
                                </c:forEach>
                            </c:if>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td class="right">
                        <label for="dateScheduled">Schedule date (run on):</label>
                    </td>
                    <td>
                        <spring:bind path="queuedReports.dateScheduled">
                            <input type="text" id="dateScheduled" name="${status.expression}" value="${status.value}"/>
                            <span class="description">
                                ${inlineInstruction}
                        </span>
                            <c:if test="${status.error}">
                                Error codes:
                                <c:forEach items="${status.errorMessages}" var="error">
                                    <c:out value="${error}"/>
                                </c:forEach>
                            </c:if>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td class="right">
                        <label for="repeatInterval">Repeat Interval:</label>
                    </td>
                    <td>
                        <spring:bind path="queuedReports.repeatInterval">
                            <input type="text" name="${status.expression}" id="repeatInterval" value="${repeatInterval}"/>
                            <c:if test="${status.error}">
                                Error codes:
                                <c:forEach items="${status.errorMessages}" var="error">
                                    <c:out value="${error}"/>
                                </c:forEach>
                            </c:if>
                        </spring:bind>

                        <select name="repeatIntervalUnits" id="repeatIntervalUnits">

                            <option value="minutes"
                                <c:if test="${units == 'minutes'}">selected</c:if> >Minutes</option>
                            <option value="hours"
                                <c:if test="${units == 'hours'}">selected</c:if> >Hours</option>
                            <option value="days"
                                <c:if test="${units == 'days'}">selected</c:if> >Days</option>

                        </select>

                        <span class="description">
                            An interval of zero (0) will make this report run only once.
                        </span>
                    </td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="visualPadding">
            <legend>Location</legend>
            <spring:bind path="queuedReports.facility.facilityId">
                <select name="${status.expression}" id="facility" size="10">
                    <c:forEach var="facility" items="${facilities}">
                        <option
                        <c:if test="${status.value==facility.facilityId}">selected</c:if> value="${facility.facilityId}">${facility.code}
                        - ${facility.name} </option>
                    </c:forEach>
                </select>
                <c:if test="${status.error}">
                    Error codes:
                    <c:forEach items="${status.errorMessages}" var="error">
                        <c:out value="${error}"/>
                    </c:forEach>
                </c:if>
            </spring:bind>
        </fieldset>

        <fieldset class="visualPadding">
            <legend>Reports</legend>
            <spring:bind path="queuedReports.reportName">
                <c:forEach var="report" items="${reportProviders}">
                    <div class="reportProvider <c:if test="${not report.visible}"> hidden</c:if>">
                        <input type="radio" name="reportName"
                               <c:if test="${status.value==report.name}">checked</c:if>
                               value="${report.name}" /> ${report.name}
                    </div>
                </c:forEach>
                <c:if test="${status.error}">
                    Error codes:
                    <c:forEach items="${status.errorMessages}" var="error">
                        <c:out value="${error}"/>
                    </c:forEach>
                </c:if>
            </spring:bind>
        </fieldset>

                <input id="submitButton"  class="visualPadding newline" type="submit" value="Queue for processing"/>

    </form>

</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
