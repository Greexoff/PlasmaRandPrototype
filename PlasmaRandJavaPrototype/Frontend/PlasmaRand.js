const mainForm = document.getElementById("mainForm");

const fileUpload = document.getElementById("fileSelection")
fileUpload.addEventListener("change", (event)=>{
    const submitBtn = document.getElementById("generateNumberBTN");
    submitBtn.disabled = !event.target.files[0];
})

mainForm.addEventListener("submit", async(event)=>
{
    event.preventDefault();
    const formData = new FormData(mainForm);
    const resultHeader = document.getElementById("generatedResult");

    try
    {
        const response = await fetch("http://localhost:8080/api/process", {method:"POST",body: formData});
        const data = await response.json();

        resultHeader.classList.remove("is-success", "is-error")

        if(data.errorMessage)
        {
            resultHeader.textContent = `${data.errorMessage}`;
            resultHeader.classList.add("is-error")
        }
        else {
            resultHeader.textContent = `Result: ${data.generatedResult}`;
            resultHeader.classList.add("is-success")

        }
    }
    catch (error)
    {
        console.error("Fetch error:", error);
        resultHeader.classList.add("is-error")
    }
})

async function getSelectObject()
{
    const response = await fetch("http://localhost:8080/api/options");
    const data = await response.json();

    const algorithmSelect = document.getElementById("algorithmsSelection");
    const generatorSelect = document.getElementById("generatorsSelection");

    data.algorithms.forEach(algorithm =>{
        const option = document.createElement("option");
        option.value = algorithm;
        option.textContent = algorithm;
        algorithmSelect.appendChild(option);
    })

    data.generators.forEach(generator =>{
        const option = document.createElement("option");
        option.value = generator;
        option.textContent = generator;
        generatorSelect.appendChild(option);
    })
}

getSelectObject();