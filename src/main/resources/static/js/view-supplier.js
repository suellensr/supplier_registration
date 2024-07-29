$(document).ready(function() {
    // Botão de logout
    $('#logout').on('click', function() {
        localStorage.removeItem('token');
        window.location.href = '/login';
    });

    // Carregar dados do fornecedor
    loadSupplierDetails();


});

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
            applyDocumentMask();
            $('#activityDescription').val(data.activityDescription);

            $('#zipCode').val(data.address.zipCode);
            $('#street').val(data.address.street);
            $('#number').val(data.address.number);
            $('#complement').val(data.address.complement);
            $('#neighborhood').val(data.address.neighborhood);
            $('#city').val(data.address.city);
            $('#state').val(data.address.state);
            $('#country').val(data.address.country);

            $('#contactName').val(data.contactName);
            $('#contactEmail').val(data.contactEmail);


            $('#contactPhones').empty();
            data.phoneNumbers.forEach(function(phone, index) {
                $('#contactPhones').append(`
                    <input type="text" class="form-control phone-input mt-2" id="phone${index}" value="${phone}" readonly>
                `);
            });
            applyPhoneMasks();
        },
        error: function() {
            alert('Erro ao carregar detalhes do fornecedor.');
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
    });
}

function deleteSupplier(supplierId) {
    console.log(localStorage.getItem("token"));
    if (confirm('Você tem certeza que quer deletar este fornecedor?')) {
        console.log(localStorage.getItem("token"));
        $.ajax({
            url: `/delete/${supplierId}`,
            type: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function () {
                alert('Fornecedor deletado com sucesso!');
                loadSuppliers(); // Recarregar a lista de fornecedores
            },
            error: function (error) {
                console.error('Erro ao deletar fornecedor:', error);
            }
        });
    }
}

function applyDocumentMask() {
    $('#documentNumber').mask(function () {
        return $('#personType').val() === 'COMPANY' ? '00.000.000/0000-00' : '000.000.000-00';
    });
}

function applyPhoneMasks() {
    $('.phone-input').each(function() {
        var phoneValue = $(this).val();
        var cleanedPhone = phoneValue.replace(/\D/g, '');

        if (cleanedPhone.length === 13) {
            $(this).mask('+00 (00) 00000-0000');
            console.log('11', cleanedPhone);
        } else if (cleanedPhone.length === 12) {
            $(this).mask('+00 (00) 0000-0000');
            console.log('10', cleanedPhone);
        } else {
            $(this).unmask();
            console.log('nenhum dos casos');
        }
    });
}