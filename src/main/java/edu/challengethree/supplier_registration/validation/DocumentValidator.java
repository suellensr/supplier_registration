package edu.challengethree.supplier_registration.validation;

import edu.challengethree.supplier_registration.exceptions.InvalidDocumentException;
import edu.challengethree.supplier_registration.model.enums.PersonType;

import java.util.InputMismatchException;

public class DocumentValidator {

    public static void validateDocument(String documentNumber, PersonType personType) {
        boolean isValid;

        switch (personType) {
            case INDIVIDUAL:
                isValid = isValidCPF(documentNumber);
                break;
            case COMPANY:
                isValid = isValidCNPJ(documentNumber);
                break;
            default:
                throw new IllegalArgumentException("Tipo de pessoa desconhecido.");
        }

        if (!isValid) {
            throw new InvalidDocumentException("Documento inválido.");
        }
    }

    private static boolean isValidCPF(String cpf) {
        // Basic CPF validation
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) {
            return false;
        }

        // FROM: https://www.devmedia.com.br/validando-o-cpf-em-uma-aplicacao-java/22097
        // Consider sequences of identical numbers as invalid CPFs
        if (cpf.equals("00000000000") ||
                cpf.equals("11111111111") ||
                cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") ||
                cpf.equals("88888888888") || cpf.equals("99999999999") ||
                (cpf.length() != 11))
            return false;

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            // Calculation of the 1st Verifying Digit
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // Convert the i-th character of CPF to a number:
                // For example, transform the character "0" into the integer 0
                // (48 is the ASCII position of "0")
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else
                dig10 = (char) (r + 48); // Convert to the corresponding numeric character

            // Calculation of the 2nd Verifying Digit
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else
                dig11 = (char) (r + 48);

            // Verify if the calculated digits match the provided digits.
            if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10)))
                return true;
            else
                return false;
        } catch (InputMismatchException erro) {
            return false;
        }
    }

    private static boolean isValidCNPJ(String cnpj) {
        // Basic CNPJ validation
        if (cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d+")) {
            return false;
        }

        //FROM: https://www.devmedia.com.br/validando-o-cnpj-em-uma-aplicacao-java/22374
        // Consider sequences of identical numbers as invalid CNPJs
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
                cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
                cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
                cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
                cnpj.equals("88888888888888") || cnpj.equals("99999999999999") ||
                (cnpj.length() != 14))
            return false;

        char dig13, dig14;
        int sm, i, r, num, peso;

        try {
            // Calculation of the 1st Verifying Digit
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // Convert the i-th character of CNPJ to a number:
                // For example, transform the character '0' into the integer 0
                // (48 is the ASCII position of '0')
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else
                dig13 = (char) ((11 - r) + 48);

            // Calculation of the 2nd Verifying Digit
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else
                dig14 = (char) ((11 - r) + 48);

            // Verify if the calculated digits match the provided digits.
            if ((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13)))
                return true;
            else
                return false;
        } catch (InputMismatchException erro) {
            return false;
        }
    }
}