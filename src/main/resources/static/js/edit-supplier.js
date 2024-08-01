$(document).ready(function() {
    // Verificar o token antes de carregar a página
    validateToken(function(isValid) {
        if (!isValid) {
            alert("Sessão expirada ou inválida. Por favor, faça login novamente.");
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
    });

    loadSupplierDetails();

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

    // Adicionar novo campo de telefone fixo
    $(document).on('click', '.add-phone-landline', function() {
        let phoneInput = `
            <div class="input-group mb-2">
                <span class="input-group-text"><i class="bi bi-telephone"></i></span>
                <input type="text" class="form-control phone-input" name="phones[]" data-type="landline" placeholder="+00 (00) 0000-0000">
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
                <input type="text" class="form-control phone-input" name="phones[]" data-type="mobile" placeholder="+00 (00) 00000-0000">
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

        submitForm(supplierData);
    });

    // Botão de logout
    $('#cancel').on('click', function() {
        loadSupplierDetails();
    });



    // Botão de logout
    $('#logout').on('click', function() {
        localStorage.removeItem('token');
        window.location.href = '/login';
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

function loadSupplierDetails() {
    const supplierId = $('#supplierId').val();
    $.ajax({
        url: `/get-supplier/${supplierId}`,
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function(data) {
            $('#supplierName').val(data.supplierName);
            $('#personType').val(data.personType);
            $('#documentNumber').val(data.documentNumber);
            //applyDocumentMask();
            $('#activityDescription').val(data.activityDescription);

            $('#cep').val(data.address.zipCode);
            $('#logradouro').val(data.address.street);
            $('#numero').val(data.address.number);
            $('#complemento').val(data.address.complement);
            $('#bairro').val(data.address.neighborhood);
            $('#localidade').val(data.address.city);
            $('#uf').val(data.address.state);
            $('#pais').val(data.address.country);

            $('#contactName').val(data.contactName);
            $('#contactEmail').val(data.contactEmail);


            $('#contactPhones .phone-input').closest('.input-group').remove();
            data.phoneNumbers.forEach(function(phone) {
                const phoneTypeIcon = phone.startsWith('+55 (') && phone.length > 17 ? 'bi-phone' : 'bi-telephone';
                $('#contactPhones').append(`
                    <div class="input-group mt-2 mb-2">
                        <span class="input-group-text"><i class="bi ${phoneTypeIcon}"></i></span>
                        <input type="text" class="form-control phone-input" name="phones[]" value="${phone}" readonly>
                        <div class="input-group-append">
                            <button class="btn btn-danger remove-phone" type="button"><i class="bi bi-trash"></i></button>
                        </div>
                    </div>
                `);
            });
            applyPhoneMasks();
        },
        error: function() {
            alert('Erro ao carregar detalhes do fornecedor.');
            window.location.href = '/home';
        }
    });
}

// Função para aplicar a máscara com base no tipo de telefone
function applyPhoneMasks() {
    $('#contactPhones input.phone-input').each(function() {
        // Remove a máscara existente
        $(this).unmask();

        // Aplica a máscara com base no atributo data-type do input
        var type = $(this).data('type');
        if (type === 'landline') {
            $(this).mask('+00 (00) 0000-0000');
        } else if (type === 'mobile') {
            $(this).mask('+00 (00) 00000-0000');
        }
    });
}

// Função global para aplicar a máscara ao campo de documento
function applyDocumentMask() {
    $('#documentNumber').mask(function () {
        return $('#personType').val() === 'COMPANY' ? '00.000.000/0000-00' : '000.000.000-00';
    });
}

function submitForm(supplierData) {
    const supplierId = $('#supplierId').val();
    $.ajax({
        url: `/supplier-update/${supplierId}`,
        type: 'PUT',
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
}
