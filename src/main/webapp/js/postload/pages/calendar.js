var list = document.getElementById("eventList");


document.getElementById("eventList").style.display = "none";
document.getElementById("listSort").style.display = "none";



document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('eventCalendar');
        calendar = new FullCalendar.Calendar(calendarEl, {
          initialView: 'dayGridMonth',
          fixedWeekCount: false
        });
        calendar.render();
      });

ajaxGet(ENDPOINT_URL + "/proposal/get?type=received&username=" + localStorage.getItem("username"), (response) => {
      receivedJsonData = JSON.parse(response).data;
      addEvents(receivedJsonData, false, document.getElementById("finalizedSelector").value, document.getElementById("respondedSelector").value);
    })

ajaxGet(ENDPOINT_URL + "/proposal/get?type=sent&username=" + localStorage.getItem("username"), (response) => {
      sentJsonData = JSON.parse(response).data;
      addEvents(sentJsonData, true, document.getElementById("finalizedSelector").value, document.getElementById("respondedSelector").value);
    })

document.getElementById("switchButton").onclick = (e) => {
    if (document.getElementById('switchButton').innerText.includes("List"))
    {
        document.getElementById("switchButton").innerText = document.getElementById("switchButton").innerText.replace("List", "Calendar");
        document.getElementById("eventList").style.display = "block";
        document.getElementById("listSort").style.display = "inline-block";
        document.getElementById("eventCalendar").style.display = "none";
    }
    else
    {
        document.getElementById("switchButton").innerText = document.getElementById("switchButton").innerText.replace("Calendar", "List");
        document.getElementById("eventList").style.display = "none";
        document.getElementById("listSort").style.display = "none";
        document.getElementById("eventCalendar").style.display = "block";
    }
}

document.getElementById("listSortSelector").onchange = (e) => {
    sortList();
}

document.getElementById("finalizedSelector").onchange = (e) => {
    resetAllEvents();
}

document.getElementById("respondedSelector").onchange = (e) => {
    resetAllEvents();
}