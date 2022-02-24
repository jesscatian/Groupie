window.addEventListener('load', (event) => {
    event.preventDefault()
    const container = document.getElementById("results")
    getResponses(container);
});