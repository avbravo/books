$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/persona"
    }).then(function(data) {
       $('.greeting-id').append(data.idpersona);
       $('.greeting-content').append(data.nombre);
    });
});