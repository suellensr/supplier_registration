$(document).ready(function() {
    $("#registerForm").on('submit', function(event) {
        event.preventDefault(); //Impede o envio normal do formulário
        const registerData = {
            email: $('#email').val(), // Obtém o valor do campo de email
            password: $('#password').val(), // Obtém o valor do campo de senha
            confirmPassword: $('#confirmPassword').val() // Obtém o valor do campo de confirmação de senha
        };

        console.log(registerData);

        $.ajax({
            url: '/auth/register', // URL para onde os dados serão enviados
            type: 'POST', // Método de envio dos dados
            contentType: 'application/json', // Tipo de conteúdo dos dados enviados
            data: JSON.stringify(registerData), // Converte os dados de registro para JSON
            success: function(response) {
                alert('Registro bem-sucedido!'); // Alerta em caso de sucesso
                $('#register-error').hide();
                // Redireciona para a página de login
                window.location.href = '/login';
            },
            error: function(xhr) {
                var errorMessage = xhr.responseText; // Lida com a resposta de erro como texto simples
                console.log("Error message: ", errorMessage); // Log para depuração
                $('#register-error').text(errorMessage).show();
            }
        });
    });
});