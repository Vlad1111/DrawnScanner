package com.example.lab1.Scripts;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

class CodeLineBit{
    private static final String[][] operators={
            {":"},
            {"||"},
            {"&&"},
            {"<=", ">=", "!=", "<", ">", "=="},
            {"=", "++", "--", "+=", "-=", "*=", "/=", "%="},
            {"+", "-"},
            {"*", "/", "%"}
    };
    String operation;
    CodeLineBit first, second;

    private static CodeLineBit createBit(char[] command, int sta, int end, int[] parentheses){
        if(sta >= end)
            return new CodeLineBit();
        if(command[sta] == '(' && parentheses[sta] == parentheses[end-1]){
            int isFull = 1;
            for(int i=sta+1; i<end;i++)
                if(parentheses[i] != parentheses[sta]){
                    isFull = 0;
                    break;
                }
            if(isFull == 1)
                return createBit(command, sta+1, end-1, parentheses);
        }

        CodeLineBit newB = new CodeLineBit();

        for (String[] operator : operators) {
            for (int i = sta; i < end - 1; i++) {
                if (parentheses[sta] != parentheses[i])
                    continue;
                for (String operand : operator) {
                    if (i + operand.length() >= end)
                        continue;

                    if (operand.charAt(0) == command[i]) {
                        boolean isThisIt = true;
                        for (int j = 1; j < operand.length(); j++)
                            if (operand.charAt(j) != command[i + j])
                                isThisIt = false;
                        if (isThisIt) {
                            newB.operation = operand;
                            newB.first = createBit(command, sta, i, parentheses);
                            newB.second = createBit(command, i + operand.length(), end, parentheses);
                            return newB;
                        }
                    }
                }
            }
        }

        if(parentheses[sta] + 1 == parentheses[end - 1]){
            int i=sta;
            for(;i<end;i++){
                if(command[i] == '('){
                    newB.operation = String.copyValueOf(command, sta, i-sta);
                    break;
                }
            }
            newB.first = createBit(command, i+1, end-1, parentheses);
            return newB;
        }
        newB.operation = String.copyValueOf(command, sta, end-sta);
        return newB;
    }

    static CodeLineBit createBit(String command){
        command = command.replaceAll("\\s","");

        int[] para = new int[command.length()];
        char[] car = command.toCharArray();
        int index = 0;

        for(int i=0;i<car.length;i++){
            if(car[i] == '(')
                index++;
            para[i] = index;
            if(car[i] == ')')
                index--;
        }
        if(index != 0)
            return new CodeLineBit();

        return createBit(car, 0, car.length, para);
    }

    private CodeLineBit(){
        operation = "";
        first = second = null;
    }

    float run(
            HashMap<String, Float> floats,
            HashMap<String, Float[]> matrix) {
        float firstF;
        float secondF = 0;
        if (second != null)
            secondF = second.run(floats, matrix);
        if (first != null)
            firstF = first.run(floats, matrix);
        else {
            firstF = secondF;
            secondF = 0;
        }
        switch (operation) {
            case "+":
                return firstF + secondF;
            case "-":
                return firstF - secondF;
            case "*":
                return firstF * secondF;
            case "/":
                if (secondF != 0)
                    return firstF + secondF;
                return firstF / 0.0000001f;
            case "%":
                if ((int) secondF != 0)
                    return (int) firstF % (int) secondF;
                return firstF;
            case "<":
                if (firstF < secondF)
                    return 1;
                return -1;
            case "<=":
                if (firstF <= secondF)
                    return 1;
                return -1;
            case ">":
                if (firstF > secondF)
                    return 1;
                return -1;
            case ">=":
                if (firstF >= secondF)
                    return 1;
                return -1;
            case "!=":
                if (firstF != secondF)
                    return 1;
                return -1;
            case "==":
                if (firstF == secondF)
                    return 1;
                return -1;
            case "=":
                if (first != null)
                    CodeBlock.assign(first.operation, secondF, floats, matrix);
                return 0;
            case "+=":
                if (first != null) {
                    float val = CodeBlock.getValueOfVariable(first.operation, floats, matrix);
                    CodeBlock.assign(first.operation, val + secondF, floats, matrix);
                }
                return 0;
            case "-=":
                if (first != null) {
                    float val = CodeBlock.getValueOfVariable(first.operation, floats, matrix);
                    CodeBlock.assign(first.operation, val - secondF, floats, matrix);
                }
                return 0;
            case "*=":
                if (first != null) {
                    float val = CodeBlock.getValueOfVariable(first.operation, floats, matrix);
                    CodeBlock.assign(first.operation, val * secondF, floats, matrix);
                }
                return 0;
            case "/=":
                if (first != null) {
                    float val = CodeBlock.getValueOfVariable(first.operation, floats, matrix);
                    if (secondF == 0)
                        secondF = 0.0000001f;
                    CodeBlock.assign(first.operation, val / secondF, floats, matrix);
                }
                return 0;
            case "%=":
                if (first != null) {
                    float val = CodeBlock.getValueOfVariable(first.operation, floats, matrix);
                    if ((int) secondF == 0)
                        return 0;
                    CodeBlock.assign(first.operation, (int) val % (int) secondF, floats, matrix);
                }
                return 0;
            case "++":
                if (first != null) {
                    float val = CodeBlock.getValueOfVariable(first.operation, floats, matrix);
                    CodeBlock.assign(first.operation, val + 1, floats, matrix);
                }
                return 0;
            case "--":
                if (first != null) {
                    float val = CodeBlock.getValueOfVariable(first.operation, floats, matrix);
                    CodeBlock.assign(first.operation, val - 1, floats, matrix);
                }
                return 0;
        }

        switch (operation) {
            case "sin":
                return (float) Math.sin(firstF);
            case "cos":
                return (float) Math.cos(firstF);
            case "tan":
                return (float) Math.tan(firstF);
            case "tanh":
                return (float) Math.tanh(firstF);
            case "ln":
                return (float) Math.log(firstF);
            case "sqrt":
                return (float) Math.sqrt(firstF);
            case "abs":
                return Math.abs(firstF);
            case "sign":
                return Math.signum(firstF);
            case "int":
                return (float) ((int) (firstF));
            case "ran":
                return CodeBlock.ran.nextFloat();
            case "floor":
                return (float) Math.floor(firstF);
        }

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (pattern.matcher(operation).matches())
            try {
                return Float.parseFloat(operation);
            } catch (Exception ex) {
                return CodeBlock.getValueOfVariable(operation, floats, matrix);
            }

        return CodeBlock.getValueOfVariable(operation, floats, matrix);
    }

    @NotNull
    @Override
    public String toString() {
        String rez = "";
        if(first != null)
            rez += first.toString() + " ";
        if(second != null)
            rez += second.toString() + " ";
        rez += operation;
        return "(" + rez + ")";
    }
}

class CodeBlock{
    static Random ran = new Random();
    CodeLineBit[] lines = null;
    CodeBlock next = null;

    HashMap<String, Float> curentFloats = null;
    HashMap<String, Float[]> curentMmatrix = null;

    CodeBlock() {

    }

    static float getValueOfVariable(String id,
                           HashMap<String, Float> curentFloats,
                           HashMap<String, Float[]> curentMmatrix){
        if(id.contains("[")){

        }
        else{
            //Float rez = curentFloats.get(id);
            //if(rez == null)
                return 0;
           // return rez;
        }
        return 0;
    }
    private float getValueOfVariable(String id){
        return getValueOfVariable(id, curentFloats, curentMmatrix);
    }

    static void assign(String id, float value,
                              HashMap<String, Float> curentFloats,
                              HashMap<String, Float[]> curentMmatrix) {
        if (id.contains("[")) {

        } else {
            //curentFloats.put(id, value);
        }
    }
    private void assign(String id, float value){
        assign(id, value, curentFloats, curentMmatrix);
    }

    void assign(String line){
        line = line.replaceAll("\\s+","");

        if(line.endsWith("++")){
            line = line.substring(0, line.length()-2);
            assign(line, getValueOfVariable(line) + 1);
        }
        else if(line.endsWith("--")){
            line = line.substring(0, line.length()-2);
            assign(line, getValueOfVariable(line) - 1);
        }

        String[] aux;
        aux = line.split("\\+=");
        if(aux.length == 2){
            String id = aux[0];
            float value = getValueOfVariable(id) + evaluateExpresion(aux[1]);
            assign(id, value);
            return;
        }
        aux = line.split("-=");
        if(aux.length == 2){
            String id = aux[0];
            float value = getValueOfVariable(id) + evaluateExpresion(aux[1]);
            assign(id, value);
            return;
        }
        aux = line.split("\\*=");
        if(aux.length == 2){
            String id = aux[0];
            float value = getValueOfVariable(id) * evaluateExpresion(aux[1]);
            assign(id, value);
            return;
        }
        aux = line.split("/=");
        if(aux.length == 2){
            String id = aux[0];
            float value = evaluateExpresion(aux[1]);
            if(value == 0)
                value = 0.0000001f;
            assign(id, getValueOfVariable(id) / value);
            return;
        }
        aux = line.split("%=");
        if(aux.length == 2){
            String id = aux[0];
            float value = ((int)getValueOfVariable(id)) % ((int)evaluateExpresion(aux[1]));
            assign(id, value);
            return;
        }
        aux = line.split("=");
        if(aux.length == 2){
            String id = aux[0];
            float value = evaluateExpresion(aux[1]);
            assign(id, value);
            return;
        }
        assign(line, 0);
    }

    Float isMathematicalExpresion(String expresion){
        int cas = 0;
        if(expresion.startsWith("sin("))
            cas = 1;
        else if(expresion.startsWith("cos("))
            cas = 2;
        else if(expresion.startsWith("tan("))
            cas = 3;
        else if(expresion.startsWith("tanh("))
            cas = 4;
        else if(expresion.startsWith("ln("))
            cas = 5;
        else if(expresion.startsWith("sqrt("))
            cas = 6;
        else if(expresion.startsWith("abs("))
            cas = 7;
        else if(expresion.startsWith("sign("))
            cas = 8;
        else if(expresion.startsWith("int("))
            cas = 9;
        else if(expresion.startsWith("ran("))
            cas = 10;

        if(cas == 0)
            return null;

        int nrP = 0;
        int curent = expresion.indexOf('(');
        int start = curent;
        for(;curent<expresion.length();curent++){
            if(expresion.charAt(curent) == '(')
                nrP++;
            else if(expresion.charAt(curent) == ')')
                nrP --;
            if(nrP == 0)
                break;
        }
        if(curent != expresion.length() - 1)
            return null;

        String exp = expresion.substring(start+1, expresion.length() - 1);

        switch (cas){
            case 1:
                return (float)Math.sin(evaluateExpresion(exp));
            case 2:
                return (float)Math.cos(evaluateExpresion(exp));
            case 3:
                return (float)Math.tan(evaluateExpresion(exp));
            case 4:
                return (float)Math.tanh(evaluateExpresion(exp));
            case 5:
                return (float)Math.log(evaluateExpresion(exp));
            case 6:
                return (float)Math.sqrt(evaluateExpresion(exp));
            case 7:
                return Math.abs(evaluateExpresion(exp));
            case 8:
                return Math.signum(evaluateExpresion(exp));
            case 9:
                return (float)((int)(evaluateExpresion(exp)));
            case 10:
                return ran.nextFloat();
        }
        return 0f;
    }

    float evaluateExpresion(String expresion){
        Float isMath = isMathematicalExpresion(expresion);
        if(isMath != null)
            return isMath;
        byte[] paranteses = new byte[expresion.length()];
        int nrP = 0;
        boolean isAllP = true;
        for(int i=0;i<expresion.length();i++){
            if(expresion.charAt(i) == '('){
                nrP++;
                paranteses[i] = 0;
            }
            else if(expresion.charAt(i) == ')'){
                nrP--;
                paranteses[i] = 0;
            }
            else{
                if(nrP == 0){
                    paranteses[i] = 1;
                    isAllP = false;
                }
                else {
                    paranteses[i] = 0;
                }
            }
        }
        if(nrP != 0)
            return  0;
        if(isAllP) {
            return evaluateExpresion(expresion.substring(1, expresion.length() - 1));
        }


        for(int i=0;i<expresion.length();i++){
            if(paranteses[i] == 0)
                continue;
            if(expresion.charAt(i) == '+'){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+1);
                return  evaluateExpresion(s1) + evaluateExpresion(s2);
            }
            else if(expresion.charAt(i) == '-'){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+1);
                return  evaluateExpresion(s1) - evaluateExpresion(s2);
            }
        }
        for(int i=0;i<expresion.length();i++){
            if(paranteses[i] == 0)
                continue;
            if(expresion.charAt(i) == '/'){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+1);
                float val2= evaluateExpresion(s2);
                if(val2 == 0)
                    val2 = 0.0000001f;
                return  evaluateExpresion(s1) / val2;
            }
            else if(expresion.charAt(i) == '*'){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+1);
                return  evaluateExpresion(s1) * evaluateExpresion(s2);
            }
            else if(expresion.charAt(i) == '%'){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+1);
                float val2= evaluateExpresion(s2);
                if(val2 == 0)
                    val2 = 1;
                return ((int)evaluateExpresion(s1)) % val2;
            }
        }
        try{
            return Float.parseFloat(expresion);
        }
        catch (Exception ex){
            return getValueOfVariable(expresion);
        }
    }

    CodeBlock(String instructions) {
        instructions = instructions.replace("\n", " ").replace("\r", " ");
        int firstA = instructions.indexOf('{');
        int firstB = firstA;
        if(firstA == -1){
            String[] lines = instructions.split(";");
            this.lines = new CodeLineBit[lines.length];
            for(int i=0;i<lines.length;i++)
                this.lines[i] = CodeLineBit.createBit(lines[i]);
            //lines = new String[1];
            //lines[0] = "DICKS";
        }
        else{
            for(;firstA>=0;firstA--){
                if(instructions.charAt(firstA) == '}' || instructions.charAt(firstA) == ';')
                    break;
            }
            if(firstA != -1){
                String lins = instructions.substring(0, firstA);
                String[] lines = lins.split(";");
                this.lines = new CodeLineBit[lines.length];
                for(int i=0;i<lines.length;i++)
                    this.lines[i] = CodeLineBit.createBit(lines[i]);
            }
            else {
                String aux = instructions.substring(0, firstB);
                aux = aux.trim();
                if(!aux.startsWith("if(") && !aux.startsWith("else") &&
                        !aux.startsWith("while(") && !aux.startsWith("for(")){
                    lines = new CodeLineBit[1];
                    lines[0] = CodeLineBit.createBit(aux);
                }
                else {
                    lines = new CodeLineBit[0];
                    //firstA = firstB - 1;
                }
            }
            String lins = instructions.substring(firstA + 1);
            lins = lins.trim();
            //lines[lines.length - 1] = lins;

            if(lins.charAt(0) == '{'){
                int nrP = 1;
                for(int i=1;i<lins.length();i++)
                {
                    if(lins.charAt(i) == '{')
                        nrP ++;
                    else if(lins.charAt(i) == '}'){
                        nrP --;
                        if(nrP == 0){
                            //this.lines = lins.substring(1,i-1).split(";");
                            //this.lines = new String[0];

                            //next = new CodeBlock(lins.substring(i+1));
                            //while(next != null && next.lines.length == 0) next = next.next;
                            char[] chars = lins.toCharArray();
                            chars[i] = ' ';
                            lins = String.valueOf(chars);
                            break;
                        }
                    }
                }
                this.next = new CodeBlock(lins.substring(1));
                while(next != null && next.lines.length == 0) next = next.next;
            }
            else if(lins.startsWith("if")){
                next = new IfBlock(lins);
            }
            else if(lins.startsWith("else")){
                next = new IfBlock(lins);
            }
            else if(lins.startsWith("for")){
                next = new ForBlock(lins);
            }else if(lins.startsWith("while")){
                next = new WhileBlock(lins);
            }
        }
    }

    void run(
            HashMap<String, Float> floats,
            HashMap<String, Float[]> matrix){
        curentFloats = floats;
        curentMmatrix = matrix;


        for (CodeLineBit line : lines) {
            line.run(floats, matrix);
        }

        if(next != null) next.run(floats, matrix);
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder rez = new StringBuilder();
        rez.append("start simple block\n");
        for (CodeLineBit line : lines)
            rez.append("> ").append(line).append("\n");
        rez.append("end simple block\n");
        if(next == null)
        {
            //rez.append(">- END OF CODE -<\n");
        }
        else rez.append(next.toString());
        return rez.toString();
    }
}

class IfBlock extends CodeBlock{
    CodeBlock then;
    private IfBlock elses = null;

    IfBlock(String instructions) {
        super();
        int firstA = instructions.indexOf('{');
        this.lines = new CodeLineBit[1];
        this.lines[0] = CodeLineBit.createBit(instructions.substring(0, firstA));
        int nrP = 1;
        for(int i=firstA+1;i<instructions.length();i++) {
            if(instructions.charAt(i) == '{')
                nrP ++;
            else if(instructions.charAt(i) == '}'){
                nrP --;
                if(nrP == 0){
                    String then = instructions.substring(firstA + 1, i);
                    this.then = new CodeBlock(then);
                    if(i != instructions.length() - 1) {
                        String next = instructions.substring(i + 1);
                        this.next = new CodeBlock(next);
                    }
                    break;
                }
            }
        }

        while(next != null && next.lines.length == 0)
            next = next.next;

        if(next!= null && next.lines.length == 1 && (next.lines[0].operation.startsWith("elseif") || next.lines[0].operation.equals("else"))){
            CodeBlock aux = next;
            next = next.next;
            aux.next = null;
            elses = (IfBlock)aux;
        }
    }

    IfBlock() {

    }

    boolean evaluateBooleanExpresion(String expresion){
        byte[] paranteses = new byte[expresion.length()];
        int nrP = 0;
        boolean isAllP = true;
        for(int i=0;i<expresion.length();i++){
            if(expresion.charAt(i) == '('){
                nrP++;
                paranteses[i] = 0;
            }
            else if(expresion.charAt(i) == ')'){
                nrP--;
                paranteses[i] = 0;
            }
            else{
                if(nrP == 0){
                    paranteses[i] = 1;
                    isAllP = false;
                }
                else {
                    paranteses[i] = 0;
                }
            }
        }
        if(nrP != 0)
            return  false;
        if(isAllP) {
            return evaluateBooleanExpresion(expresion.substring(1, expresion.length() - 1));
        }


        for(int i=0;i<expresion.length() - 1;i++){
            if(paranteses[i] == 0)
                continue;
            if(expresion.charAt(i) == '|' && expresion.charAt(i+1) == '|'){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+2);
                return  evaluateBooleanExpresion(s1) || evaluateBooleanExpresion(s2);
            }
            else if(expresion.charAt(i) == '&' && expresion.charAt(i+1) == '&'){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+2);
                return  evaluateBooleanExpresion(s1) && evaluateBooleanExpresion(s2);
            }
        }
        for(int i=0;i<expresion.length() - 1;i++){
            if(paranteses[i] == 0)
                continue;
            if(expresion.charAt(i) == '<' && expresion.charAt(i+1) == '='){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+2);
                return  evaluateExpresion(s1) <= evaluateExpresion(s2);
            }
            else if(expresion.charAt(i) == '>' && expresion.charAt(i+1) == '='){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+2);
                return  evaluateExpresion(s1) >= evaluateExpresion(s2);
            }
            else if(expresion.charAt(i) == '!' && expresion.charAt(i+1) == '='){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+2);
                return  evaluateExpresion(s1) != evaluateExpresion(s2);
            }
            else if(expresion.charAt(i) == '<'){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+1);
                return  evaluateExpresion(s1) < evaluateExpresion(s2);
            }
            else if(expresion.charAt(i) == '>'){
                String s1 = expresion.substring(0, i);
                String s2 = expresion.substring(i+1);
                return  evaluateExpresion(s1) > evaluateExpresion(s2);
            }
        }
        try{
            return evaluateExpresion(expresion) > 0;
        }
        catch (Exception ex){
            return false;
        }
    }

    @Override
    void run(HashMap<String, Float> floats, HashMap<String, Float[]> matrix) {
        this.curentFloats = floats;
        this.curentMmatrix = matrix;

        if(!this.lines[0].operation.equals("else")) {
            /*String boolExp = this.lines[0].substring(this.lines[0].indexOf('(') + 1, this.lines[0].length() - 1).trim();
            boolExp = boolExp.replaceAll("\\s+","");
            if (evaluateBooleanExpresion(boolExp))
                this.then.run(floats, matrix);
            else if(this.elses != null)
                this.elses.run(floats, matrix);*/
            if (this.lines[0].first.run(floats, matrix) > 0)
                this.then.run(floats, matrix);
            else if(this.elses != null)
                this.elses.run(floats, matrix);
        }
        else this.then.run(floats, matrix);
        if(next != null) next.run(floats, matrix);
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder rez = new StringBuilder();
        rez.append("start if block\n");
        for (CodeLineBit line : lines) rez.append("> ").append(line).append("\n");

        if(then != null)
            rez.append("{\n").append(then.toString()).append("}\n");

        if(elses != null)
            rez.append("vvvvvvvvvvv\n").append(elses);
        rez.append("end if block\n");

        if(next == null)
        {
            //rez.append(">- END OF CODE -<\n");
        }
        else rez.append(next.toString());
        return rez.toString();
    }
}

class ForBlock extends IfBlock{
    ForBlock(String instructions) {
        super();
        int firstA = instructions.indexOf('{');
        this.lines = new CodeLineBit[1];
        this.lines[0] = CodeLineBit.createBit(instructions.substring(0, firstA));
        int nrP = 1;
        for(int i=firstA+1;i<instructions.length();i++) {
            if(instructions.charAt(i) == '{')
                nrP ++;
            else if(instructions.charAt(i) == '}'){
                nrP --;
                if(nrP == 0){
                    String then = instructions.substring(firstA + 1, i);
                    this.then = new CodeBlock(then);
                    if(i != instructions.length() - 1) {
                        String next = instructions.substring(i + 1);
                        this.next = new CodeBlock(next);
                    }
                    break;
                }
            }
        }
    }

    @Override
    void run(HashMap<String, Float> floats, HashMap<String, Float[]> matrix) {
        this.curentFloats = floats;
        this.curentMmatrix = matrix;

        //String[] parts = this.lines[0].substring(this.lines[0].indexOf('(') + 1, this.lines[0].length() - 1).split(":");

        lines[0].first.run(floats, matrix);
        for(;lines[0].second.first.run(floats, matrix) > 0;lines[0].second.second.run(floats, matrix)){
            if(this.then != null)
                this.then.run(floats, matrix);
        }

        if(next != null) next.run(floats, matrix);
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder rez = new StringBuilder();
        rez.append("start for block\n");
        for (CodeLineBit line : lines) rez.append("> ").append(line).append("\n");

        if(then != null)
            rez.append("{\n").append(then.toString()).append("}\n");
        rez.append("end for block\n");

        if(next == null)
        {
            //rez.append(">- END OF CODE -<\n");
        }
        else rez.append(next.toString());
        return rez.toString();
    }
}
class WhileBlock extends ForBlock{

    WhileBlock(String instructions) {
        super(instructions);
    }

    @Override
    void run(HashMap<String, Float> floats, HashMap<String, Float[]> matrix) {
        this.curentFloats = floats;
        this.curentMmatrix = matrix;

        /*this.lines[0] = this.lines[0].trim();

        String part = this.lines[0].substring(this.lines[0].indexOf('(') + 1, this.lines[0].length() - 1);
*/
        while(lines[0].run(floats, matrix) > 0) {
            this.then.run(floats, matrix);
        }

        if(next != null) next.run(floats, matrix);
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder rez = new StringBuilder();
        rez.append("start while block\n");
        for (CodeLineBit line : lines) rez.append("> ").append(line).append("\n");

        if(then != null)
            rez.append("{\n").append(then.toString()).append("}\n");
        rez.append("end while block\n");

        if(next == null)
        {
            //rez.append(">- END OF CODE -<\n");
        }
        else rez.append(next.toString());
        return rez.toString();
    }
}

public class Script {
    private String color;
    private String name;

    private CodeBlock colorBlock = null;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.colorBlock = null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name.trim();
        this.name = name;
    }

    public Script() {
        color = "";
    }

    public Script(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Script{" +
                "color='" + color + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getColor(int color, int c1, int c2) {
        if (colorBlock == null) {
            try {
                colorBlock = new CodeBlock(this.color);
            } catch (Exception ex) {
                return color;
            }
        }
        HashMap<String, Float> floats = new HashMap<String, Float>();
        HashMap<String, Float[]> matrix = new HashMap<String, Float[]>();
        floats.put("color_a", (float) Color.alpha(color));
        floats.put("color_r", (float) Color.red(color));
        floats.put("color_g", (float) Color.green(color));
        floats.put("color_b", (float) Color.blue(color));

        floats.put("c1_a", (float) Color.alpha(c1));
        floats.put("c1_r", (float) Color.red(c1));
        floats.put("c1_g", (float) Color.green(c1));
        floats.put("c1_b", (float) Color.blue(c1));

        floats.put("c2_a", (float) Color.alpha(c2));
        floats.put("c2_r", (float) Color.red(c2));
        floats.put("c2_g", (float) Color.green(c2));
        floats.put("c2_b", (float) Color.blue(c2));

        //Pair<TreeMap<String, Float>, TreeMap<String, Float[][]>> rez = colorBlock.run(floats, matrix);
        colorBlock.run(floats, matrix);
        Float a = floats.get("color_a");
        Float r = floats.get("color_r");
        Float g = floats.get("color_g");
        Float b = floats.get("color_b");
        if (a == null || r == null || g == null || b == null)
            return color;

        //return Color.BLACK;
        return Color.argb(a / 255, r / 255, g / 255, b / 255);
    }

    public String getColorBlockInstructions() {
        if (colorBlock == null)
            colorBlock = new CodeBlock(this.color);
        return colorBlock.toString();
    }

    public Bitmap getImage(Bitmap image, int c1, int c2) {
        if (colorBlock == null) {
            try {
                colorBlock = new CodeBlock(this.color);
            } catch (Exception ex) {
                return image;
            }
        }
        int[] colors = new int[image.getWidth() * image.getHeight()];
        image.getPixels(colors, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        HashMap<String, Float> floats = new HashMap<String, Float>();
        HashMap<String, Float[]> matrix = new HashMap<String, Float[]>();

        floats.put("c1_a", (float) Color.alpha(c1));
        floats.put("c1_r", (float) Color.red(c1));
        floats.put("c1_g", (float) Color.green(c1));
        floats.put("c1_b", (float) Color.blue(c1));

        floats.put("c2_a", (float) Color.alpha(c2));
        floats.put("c2_r", (float) Color.red(c2));
        floats.put("c2_g", (float) Color.green(c2));
        floats.put("c2_b", (float) Color.blue(c2));

        floats.put("img_wid", (float) image.getWidth());
        floats.put("img_hei", (float) image.getHeight());

        Bitmap aux = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (int i = 0; i < aux.getWidth(); i++)
                for (int j = 0; j < aux.getHeight(); j++) {
                    int location = i * aux.getHeight() + j;
                    int color = colors[location];
                    floats.put("color_a", (float) Color.alpha(color));
                    floats.put("color_r", (float) Color.red(color));
                    floats.put("color_g", (float) Color.green(color));
                    floats.put("color_b", (float) Color.blue(color));
                    floats.put("wid_pos", (float) i);
                    floats.put("hei_pos", (float) j);
                    colorBlock.run(floats, matrix);
                    Float a = floats.get("color_a");
                    Float r = floats.get("color_r");
                    Float g = floats.get("color_g");
                    Float b = floats.get("color_b");
                    if (a != null && r != null && g != null && b != null)
                        colors[location] = Color.argb(a / 255, r / 255, g / 255, b / 255);
                    else colors[location] = Color.BLACK;
                }
        }
        aux.setPixels(colors, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        return aux;
    }
}
