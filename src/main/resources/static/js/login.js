$(document).ready(function() {
    $("#loginForm").on('submit', function(event) {
        event.preventDefault(); //Impede o envio normal do formulário
        const loginData = {
            email: $('#email').val(), // Obtém o valor do campo de email
            password: $('#password').val(), // Obtém o valor do campo de senha
        };

        console.log(loginData);

        $.ajax({
            url: '/auth/login', // URL para onde os dados serão enviados
            type: 'POST', // Método de envio dos dados
            contentType: 'application/json', // Tipo de conteúdo dos dados enviados
            data: JSON.stringify(loginData), // Converte os dados de registro para JSON
            success: function(response) {
                // Armazena o token JWT para futuras requisições
                localStorage.setItem('token', response.token);
                console.log(response.token)
                $('#login-error').hide();
                // Redireciona para a página principal ou dashboard
                window.location.href = '/home';
                alert('Login bem-sucedido!'); // Alerta em caso de sucesso
            },
            error: function(xhr) {
                var errorMessage = xhr.responseText; // Lida com a resposta de erro como texto simples
                console.log("Error message: ", errorMessage); // Log para depuração
                $('#login-error').text(errorMessage).show(); // Certifique-se de que o ID esteja correto
            }
        });
    });
});
