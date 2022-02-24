var receivedJsonData;
var sentJsonData;
var outstandingChecks = [];


const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}


const account = () => {
  window.location.href = "./account.html"
}

const pendingInvites = () => {
  window.location.href = "./pendinginvites.html"
}

const proposalResponse = () => {
  window.location.href = "./proposalResponse.html"
}

var calendar;

function clearList()
{
  while (list.children.length > 0)
  {
    list.removeChild(list.firstChild);
  }
}

function clearCal()
{
  var calEvents = calendar.getEvents();
  for (let i = 0; i < calEvents.length; i++)
  {
    calEvents[i].remove();
  }
}

function addEventToList(event)
{
  let eventItem = document.createElement("div");
  eventItem.className = "listItem";

  let eventDate = document.createElement("p");
  eventDate.className = "subtitle";
  eventDate.innerHTML = moment(event.date).format("MMMM Do YYYY")
  eventItem.append(eventDate);

  let eventName = document.createElement("a");
  eventName.href = event.url;
  eventName.innerHTML = event.name;
  eventItem.append(eventName);

  let eventTime = document.createElement("b");
  eventTime.innerHTML = event.time;
  eventItem.append(eventTime);

  eventItem.setAttribute("datetime", event.date + "T" + event.time);

  list.append(eventItem);
}

function sortList()
{
  var oldestFirst = document.getElementById("listSortSelector").value == "oldestFirst";
  var sortFunc;
  if (oldestFirst)
  {
    sortFunc = function(event1, event2) {
      let event1Time = moment(event1.getAttribute("datetime"));
      let event2Time = moment(event2.getAttribute("datetime"));
      if (event1Time.isBefore(event2Time))
      {
        return -1;
      }
      else if (event1Time.isSame(event2Time))
      {
        return 0;
      }
      else
      {
        return 1;
      }
    }
  }
  else
  {
    sortFunc = function(event1, event2) {
      let event1Time = moment(event1.getAttribute("datetime"));
      let event2Time = moment(event2.getAttribute("datetime"));
      if (event1Time.isBefore(event2Time))
      {
        return 1;
      }
      else if (event1Time.isSame(event2Time))
      {
        return 0;
      }
      else
      {
        return -1;
      }
    }
  }

  var eventArray = Array.from(list.children);
  eventArray.sort(sortFunc);

  clearList();
  for (let i = 0; i < eventArray.length; i++)
  {
    list.append(eventArray[i]);
  }
}

function addEventToCalendar(event)
{
  let calEvent = {
    title: event.name,
    start: event.date + 'T' + event.time,
    end: event.date + 'T' + event.time + '+01:00:00',
    url: event.url
  };
  calendar.addEvent(calEvent);
}

function conditionalAdd(event, ifResponseExistsIs)
{
  outstandingChecks.push(event.eventID);
  ajaxGet(ENDPOINT_URL + "/response/get?event_id=" + event.eventID + "&username=" + localStorage.getItem("username"), (response) => {
    console.log(JSON.parse(response).status + " for event " + event.name + " with " + ifResponseExistsIs);
    if (JSON.parse(response).status == ifResponseExistsIs)
    {
      addEventToList(event);
      addEventToCalendar(event);
    }
    outstandingChecks.splice(outstandingChecks.indexOf(event.eventID), 1);
    if (outstandingChecks.length == 0)
    {
      sortList();
    }
  });
}

function addEvents(proposals, sent, finalized, respondedSelector)
{
  for (let i = 0; i < proposals.length; i++)
  {
    if (proposals[i].finalizedEventID == null && finalized != "onlyFinalized")
    {
      for (let j = 0; j < proposals[i].events.length; j++)
      {
        if (sent || respondedSelector == "both")
        {
          addEventToList(proposals[i].events[j]);
          addEventToCalendar(proposals[i].events[j]);
        }
        else
        {
          conditionalAdd(proposals[i].events[j], respondedSelector == "onlyResponded");
        }
      }
    }
    else if (finalized != "onlyNot")
    {
      for (let j = 0; j < proposals[i].events.length; j++)
      {
        if (proposals[i].events[j].eventID == proposals[i].finalizedEventID)
        {
          if (sent || respondedSelector == "both")
          {
            addEventToList(proposals[i].events[j]);
            addEventToCalendar(proposals[i].events[j]);
          }
          else
          {
            conditionalAdd(proposals[i].events[j], respondedSelector == "onlyResponded");
          }
        }
      }
    }
  }
  sortList();
}

function resetAllEvents()
{
  clearList();
  clearCal();
  addEvents(receivedJsonData, false, document.getElementById("finalizedSelector").value, document.getElementById("respondedSelector").value);
  addEvents(sentJsonData, true, document.getElementById("finalizedSelector").value, document.getElementById("respondedSelector").value);
}