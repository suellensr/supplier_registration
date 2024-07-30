$(document).ready(function() {
    // Verificar o token antes de carregar a página
    validateToken(function(isValid) {
        if (isValid) {
            console.log('Token válido!')
        } else {
            alert("Sessão expirada ou inválida. Por favor, faça login novamente.");
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
    });

    // Aplicar máscaras
    $('#cep').mask('00000-000');

    // Máscara para CPF e CNPJ
    // Inicialmente aplicar a máscara como 'COMPANY'
    $('#personType').val('COMPANY'); // Define o valor inicial como 'COMPANY'
    applyDocumentMask(); // Aplica a máscara com base no valor inicial

    // Atualizar máscara quando o tipo de pessoa muda
    $('#personType').change(function() {
        applyDocumentMask(); // Aplica a máscara com base no novo valor
    });

    // Inicialmente aplica as máscaras existentes
    applyPhoneMasks();

    // Adicionar novo campo de telefone fixo
    $(document).on('click', '.add-phone-landline', function() {
        let phoneInput = `
            <div class="input-group mb-2">
                <span class="input-group-text"><i class="bi bi-telephone"></i></span>
                <input type="text" class="form-control phone-input" name="phones[]" placeholder="+00 (00) 0000-0000">
                <div class="input-group-append">
                   <button class="btn btn-danger remove-phone" type="button"><i class="bi bi-trash"></i></button>
                </div>
            </div>`;
        $('#contactPhones').append(phoneInput);
        // Aplica a máscara ao novo campo
        applyPhoneMasks();
    });

    // Adicionar novo campo de telefone móvel
    $(document).on('click', '.add-phone-mobile', function() {
        let phoneInput = `
            <div class="input-group mb-2">
                <span class="input-group-text"><i class="bi bi-phone"></i></span>
                <input type="text" class="form-control phone-input" name="phones[]" placeholder="+00 (00) 00000-0000">
                <div class="input-group-append">
                   <button class="btn btn-danger remove-phone" type="button"><i class="bi bi-trash"></i></button>
                </div>
            </div>`;
        $('#contactPhones').append(phoneInput);
        // Aplica a máscara ao novo campo
        applyPhoneMasks();
    });


    // Remover campo de telefone
    $(document).on('click', '.remove-phone', function() {
        if ($('#contactPhones .input-group').length > 1) {
            $(this).closest('.input-group').remove();
        } else {
            alert("Você deve manter pelo menos um campo de telefone.");
        }
    });

    // Converter nome do fornecedor e do contato para maiúsculas
    $('input, textarea').on('input', function() {
        if ($(this).attr('id') === 'supplierName' || $(this).attr('id') === 'contactName') {
            $(this).val($(this).val().toUpperCase());
        }
    });

    // Preencher endereço ao digitar o CEP
    $('#cep').on('blur', function() {
        const cep = $(this).val().replace(/\D/g, ''); // Remove caracteres não numéricos
        if (cep.length === 8) {
            $.ajax({
                url: `https://viacep.com.br/ws/${cep}/json/`,
                type: 'GET',
                success: function(data) {
                    if (!data.erro) {
                        $('#logradouro').val(data.logradouro);
                        $('#bairro').val(data.bairro);
                        $('#localidade').val(data.localidade);
                        $('#uf').val(data.uf);
                        $('#pais').val('Brasil'); // Preenche o país como Brasil, mas posso tirar
                    } else {
                        alert('CEP não encontrado.');
                    }
                },
                error: function() {
                    alert('Erro ao buscar endereço. Tente novamente.');
                }
            });
        }
    });

    // Salvar registro
    $('#supplierForm').on('submit', function(event) {
        event.preventDefault(); // Evita o comportamento padrão de submissão do formulário
        console.log($('#localidade').val())
        console.log($('#uf').val())
        console.log($('#personType').val())
        console.log($('#documentNumber').val())
        const supplierData = {
            supplierName: $('#supplierName').val(),
            contactName: $('#contactName').val(),
            contactEmail: $('#contactEmail').val(),
            personType: $('#personType').val(),
            documentNumber: $('#documentNumber').val(),
            phoneNumbers: $('input[name="phones[]"]').map(function() { return $(this).val(); }).get(),
            addressDTO: {
                cep: $('#cep').val(),
                logradouro: $('#logradouro').val().toUpperCase(),
                numero: $('#numero').val(),
                complemento: $('#complemento').val(),
                bairro: $('#bairro').val(),
                localidade: $('#localidade').val(),
                uf: $('#uf').val(),
                pais: $('#pais').val()
            },
            activityDescription: $('#activityDescription').val()
        };

        $.ajax({
            url: '/supplier-register',
            type: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            contentType: 'application/json',
            data: JSON.stringify(supplierData),
            success: function(response) {
                window.location.href = '/home'; // Voltar para a Home
            },
            error: function(xhr) {
                console.log()
                errorMessage = xhr.responseJSON.message;
                console.log("Error message: ", errorMessage); // Log para depuração
                alert('Erro ao cadastrar fornecedor: ' + errorMessage);
            }
        });
    });

    // Cancelar redirecionamento
    $('#cancel').on('click', function() {
        window.location.href = '/home'; // Voltar para a Home
    });
});

function validateToken(callback) {
    $.ajax({
        url: '/auth/validate-token',
        type: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function(response) {
            callback(true); // Token válido
        },
        error: function(error) {
            console.error('Token validation error:', error); // Adicione um log para depuração
            callback(false); // Token inválido ou expirado
        }
    });
}

// Função global para aplicar a máscara ao campo de documento
function applyDocumentMask() {
    $('#documentNumber').mask(function () {
        return $('#personType').val() === 'COMPANY' ? '00.000.000/0000-00' : '000.000.000-00';
    });
}

function applyPhoneMasks() {
    // Seleciona todos os inputs de telefone
    $('#contactPhones input').each(function() {
        // Aplica a máscara padrão
        $(this).mask('+00 (00) 00000-0000');

        // Evento de input para ajustar a máscara conforme o usuário digita
        $(this).on('input', function() {
            var phoneValue = $(this).val();
            console.log(phoneValue);
            var cleanedPhone = phoneValue.replace(/\D/g, '');

            if (cleanedPhone.length === 13) {
                $(this).mask('+00 (00) 00000-0000');
            } else if (cleanedPhone.length === 12) {
                $(this).mask('+00 (00) 0000-0000');
            }
        });
    });
}