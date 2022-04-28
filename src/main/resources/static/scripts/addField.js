//botao "novo horario" em cadastro
//Procurar o botao novo horario
document.querySelector("#add-time") 
// Ao clicar no botao novo horario
.addEventListener('click', cloneField)
// clonar o espaco de novo horario para poder adicionar mais um horario
function cloneField() {
    //duplicar os campos schedule item
    const newFieldContainer = document.querySelector('.schedule-item').cloneNode(true) 
    //Nodes significa estrutura html, cloneNode vai clonar aquilo pra constante fields
    const fields = newFieldContainer.querySelectorAll('input') 
    //seleciona todos inputs do newfieldcontainer e armazena como vetor em fields
    //agora quero que esvazie o campo que eu clonei:
    fields.forEach(function (field) {
        field.value= ""  

    }) 
    //colocando na pagina, preciso especificar onde colocar (na area dos schedule-items)
    document.querySelector('#schedule-items').appendChild(newFieldContainer) 
    //appendchild adiciona um filho na regiao especificada, no caso vai adicionar o clone armazenado em fields

}