document.querySelector("#country").onpaste = () => {
    const element = document.getElementById("country");
    country(element);
}

document.querySelector("#search-button").onclick = (e) => {
    e.preventDefault()
    const keywords = document.getElementById("keywords")
    const genre = document.getElementById("genre")
    const country = document.getElementById("country")
    const startDate = document.getElementById("start-date")
    const endDate = document.getElementById("end-date")
    const errMsg = document.querySelector(".error-msg")
    const code = country.value.trim().toUpperCase()
    const container = document.getElementById("results")

    search(keywords, genre, country, startDate, endDate, errMsg, code, container);
}