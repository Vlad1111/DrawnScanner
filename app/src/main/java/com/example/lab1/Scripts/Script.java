package com.example.lab1.Scripts;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.FloatProperty;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.io.Console;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.security.auth.Subject;

import kotlin.contracts.Returns;

class CodeLineBit{
    private String operation;
    private CodeLineBit first, second;

    Float isMathematicalExpresion(String expresion, float ){
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
    CodeLineBit(String command){
        first = second = null;
        String[] paart = command.split("=");
        if(paart.length == 1)
            operation
    }
}

class CodeBlock{
    static Random ran = new Random();
    String[] lines = null;
    CodeBlock next = null;

    HashMap<String, Float> curentFloats = null;
    HashMap<String, Float[]> curentMmatrix = null;

    CodeBlock() {

    }

    float getValueOfVariable(String id){
        if(curentMmatrix == null || curentFloats == null)
            return  0;
        if(id.contains("[")){

        }
        else{
            Float rez = curentFloats.get(id);
            System.out.println(id + " -> " + rez);
            if(rez == null)
                return 0;
            return rez;
        }
        return 0;
    }

    private void assigne(String id, float value){
        if(curentMmatrix == null || curentFloats == null)
            return;
        if(id.contains("[")){

        }
        else{
            curentFloats.put(id, value);
        }
    }

    void assigne(String line){
        line = line.replaceAll("\\s+","");

        if(line.endsWith("++")){
            line = line.substring(0, line.length()-2);
            assigne(line, getValueOfVariable(line) + 1);
        }
        else if(line.endsWith("--")){
            line = line.substring(0, line.length()-2);
            assigne(line, getValueOfVariable(line) - 1);
        }

        String[] aux;
        aux = line.split("\\+=");
        if(aux.length == 2){
            String id = aux[0];
            float value = getValueOfVariable(id) + evaluateExpresion(aux[1]);
            assigne(id, value);
            return;
        }
        aux = line.split("-=");
        if(aux.length == 2){
            String id = aux[0];
            float value = getValueOfVariable(id) + evaluateExpresion(aux[1]);
            assigne(id, value);
            return;
        }
        aux = line.split("\\*=");
        if(aux.length == 2){
            String id = aux[0];
            float value = getValueOfVariable(id) * evaluateExpresion(aux[1]);
            assigne(id, value);
            return;
        }
        aux = line.split("/=");
        if(aux.length == 2){
            String id = aux[0];
            float value = evaluateExpresion(aux[1]);
            if(value == 0)
                value = 0.0000001f;
            assigne(id, getValueOfVariable(id) / value);
            return;
        }
        aux = line.split("%=");
        if(aux.length == 2){
            String id = aux[0];
            float value = ((int)getValueOfVariable(id)) % ((int)evaluateExpresion(aux[1]));
            assigne(id, value);
            return;
        }
        aux = line.split("=");
        if(aux.length == 2){
            String id = aux[0];
            float value = evaluateExpresion(aux[1]);
            assigne(id, value);
            return;
        }
        assigne(line, 0);
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
            lines = instructions.split(";");
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
                lines = lins.split(";");
            }
            else {
                String aux = instructions.substring(0, firstB);
                aux = aux.trim();
                if(!aux.startsWith("if(") && !aux.startsWith("else") &&
                        !aux.startsWith("while(") && !aux.startsWith("for(")){
                    lines = new String[1];
                    lines[0] = aux;
                }
                else {
                    lines = new String[0];
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


        for (String line : lines) {
            assigne(line);
        }

        if(next != null) next.run(floats, matrix);
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder rez = new StringBuilder();
        rez.append("start simple block\n");
        for (String line : lines) rez.append("> ").append(line).append("\n");
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
        this.lines = new String[1];
        this.lines[0] = instructions.substring(0, firstA);
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

        if(next!= null && next.lines.length == 1 && (next.lines[0].startsWith("else if") || next.lines[0].trim().equals("else"))){
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

        this.lines[0] = this.lines[0].trim();
        if(!this.lines[0].equals("else")) {
            String boolExp = this.lines[0].substring(this.lines[0].indexOf('(') + 1, this.lines[0].length() - 1).trim();
            boolExp = boolExp.replaceAll("\\s+","");
            if (evaluateBooleanExpresion(boolExp))
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
        for (String line : lines) rez.append("> ").append(line).append("\n");

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
        this.lines = new String[1];
        this.lines[0] = instructions.substring(0, firstA);
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

        this.lines[0] = this.lines[0].trim();

        String[] parts = this.lines[0].substring(this.lines[0].indexOf('(') + 1, this.lines[0].length() - 1).split(":");

        this.assigne(parts[0]);
        for(;evaluateBooleanExpresion(parts[1]);this.assigne(parts[2])){
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
        for (String line : lines) rez.append("> ").append(line).append("\n");

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

        this.lines[0] = this.lines[0].trim();

        String part = this.lines[0].substring(this.lines[0].indexOf('(') + 1, this.lines[0].length() - 1);

        while(evaluateBooleanExpresion(part)) {
            this.then.run(floats, matrix);
        }

        if(next != null) next.run(floats, matrix);
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder rez = new StringBuilder();
        rez.append("start while block\n");
        for (String line : lines) rez.append("> ").append(line).append("\n");

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
                    System.out.println(a +" " + r +" " +g+ " "+b);
                    if (a != null && r != null && g != null && b != null)
                        colors[location] = Color.argb(a / 255, r / 255, g / 255, b / 255);
                    else colors[location] = Color.BLACK;
                }
        }
        aux.setPixels(colors, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        return aux;
    }
}
