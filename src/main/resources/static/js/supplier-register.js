$(document).ready(function() {
    // Aplicar máscaras
    $('#zipCode').mask('00000-000');
    // Máscara para CPF e CNPJ
    $('#documentNumber').mask(function () {
        return $('#personType').val() === 'COMPANY' ? '00.000.000/0000-00' : '000.000.000-00';
    });
    $('#contactPhones input').mask('(00) 00000-0000');

    // Adicionar novo campo de telefone
    $(document).on('click', '.add-phone', function() {
        let phoneInput = `
            <div class="input-group mb-2">
                <input type="text" class="form-control" name="phones[]">
                <div class="input-group-append">
                    <button class="btn btn-primary add-phone" type="button">Adicionar</button>
                    <button class="btn btn-danger remove-phone" type="button">Remover</button>
                </div>
            </div>`;
        $('#contactPhones').append(phoneInput);
        $('#contactPhones input').last().mask('(00) 00000-0000');
    });

    // Remover campo de telefone
    $(document).on('click', '.remove-phone', function() {
        $(this).closest('.input-group').remove();
    });

    // Converter texto de entrada para maiúsculas, exceto o email
    $('input, textarea').on('input', function() {
        if ($(this).attr('id') !== 'contactEmail') {
            $(this).val($(this).val().toUpperCase());
        }
    });

    // Preencher endereço ao digitar o CEP
    $('#cep').on('blur', function() {
        const cep = $(this).val().replace(/\D/g, ''); // Remove caracteres não numéricos
        if (cep.length === 8) {
            $.ajax({
                url: `https://viacep.com.br/ws/${zipCode}/json/`,
                type: 'GET',
                success: function(data) {
                    if (!data.erro) {
                        $('#logradouro').val(data.logradouro);
                        $('#bairro').val(data.bairro);
                        $('#cidade').val(data.cidade);
                        $('#uf').val(data.uf);
                        $('#country').val('Brasil'); // Preenche o país como Brasil, mas posso tirar
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
        console.log($('#cidade').val())
        console.log($('#uf').val())
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
                cidade: $('#cidade').val(),
                uf: $('#uf').val(),
                pais: $('#pais').val()
            },
            activityDescription: $('#activityDescription').val()
        };

        $.ajax({
            url: '/supplier-registration/supplier-register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(supplierData),
            success: function(response) {
                alert('Fornecedor cadastrado com sucesso');
                window.location.href = '/home'; // Voltar para a Home
            },
            error: function(xhr, status, error) {
                alert('Erro ao cadastrar fornecedor: ' + xhr.responseText);
            }
        });
    });

    // Cancelar redirecionamento
    $('#cancel').on('click', function() {
        window.location.href = '/home'; // Voltar para a Home
    });
});