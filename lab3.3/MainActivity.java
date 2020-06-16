package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    EditText a;
    EditText b;
    EditText c;
    EditText d;
    EditText y;
    Button calc;
    TextView res;
    EditText mutation;

    int yInt;
    int aInt;
    int bInt;
    int cInt;
    int dInt;
    double mutationDouble;

    int[] result = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        y = findViewById(R.id.y);
        calc = findViewById(R.id.button);
        res = findViewById(R.id.text);
        mutation = findViewById(R.id.mutation);

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text1 = a.getText().toString();
                String text2 = b.getText().toString();
                String text3 = c.getText().toString();
                String text4 = d.getText().toString();
                String text5 = y.getText().toString();
                String text6 = mutation.getText().toString();

                aInt = Integer.parseInt(text1);
                bInt = Integer.parseInt(text2);
                cInt = Integer.parseInt(text3);
                dInt = Integer.parseInt(text4);
                yInt = Integer.parseInt(text5);
                mutationDouble = Double.parseDouble(text6);
                res.setText(geneticAlgorithm());
            }
        });

    }

    private String geneticAlgorithm() {
        Random rand = new Random();
        int size = 5;
        int counter = 0;
        ArrayList<ArrayList<Integer>> population = new ArrayList<>();

        //заполнение первой популяции
        for (int i = 0; i < size; i++) {
            ArrayList<Integer> tmp = new ArrayList<>(4);
            for (int j = 0; j < 4; j++) {
                tmp.add(rand.nextInt(yInt / 2));
            }
            population.add(tmp);
        }

        //основной цикл генетического алгоритма.
        while (true) {
            double sumDeltas = 0;
            int[] deltas = new int[size];

            if (counter == 10)
            {
                return "10 iterations passed";
            }

            counter++;
            //расчет дельты для всех генотипов
            for (int i = 0; i < size; i++) {
                deltas[i] = fitness(population.get(i));
                if (deltas[i] == 0) {
                    return " х1 = " + population.get(i).get(0) + ", " +
                            " х2 = " + population.get(i).get(1) + ", " +
                            " х3 = " + population.get(i).get(2) + ", " +
                            " х4 = " + population.get(i).get(3);
                }
                sumDeltas += 1.0 / deltas[i];
            }

            double[] parentPercents = new double[size];

            for (int i = 0; i < size; i++) {
                parentPercents[i] = 1.0 / (deltas[i] * sumDeltas) * 100;
            }

            double[] forChildren = new double[size + 1];
            forChildren[0] = 0;

            for (int i = 0; i < size - 1; i++) {
                forChildren[i + 1] = forChildren[i] + parentPercents[i];
            }

            forChildren[size - 1] = 100;


            //выбор родителя для следующей генерации
            ArrayList<ArrayList<Integer>> parents = new ArrayList<>();

            int forParent;

            for (int i = 0; i < size * 2; i++) {
                forParent = rand.nextInt(100);
                for (int j = 0; j < forChildren.length - 1; j++) {
                    if (forChildren[j] <= forParent && forChildren[j + 1] > forParent) {
                        parents.add(population.get(j));
                    }
                }
            }

            population.clear();

            //создание следующей генерации
            for (int i = 0; i < parents.size(); i = i + 2) {
                ArrayList<Integer> tmp = new ArrayList<>(4);
                forParent = (int)(Math.random() * 3 + 1);
                for (int j = 0; j < forParent; j++) {
                    tmp.add(parents.get(i).get(j));
                }
                for (int j = forParent; j < 4; j++) {
                    tmp.add(parents.get(i + 1).get(j));
                }
                population.add(tmp);
            }

            parents.clear();

            //мутация
            if (Math.random() < mutationDouble) {
                int countOfChanges = rand.nextInt(size);
                for (int i = 0; i < countOfChanges; i++) {
                    population.get(rand.nextInt(size)).set(rand.nextInt(4),
                                    rand.nextInt(yInt / 2));
                }
            }
        }
    }

    //фитнес функция
    private int fitness(ArrayList<Integer> oneIndividual) {
        return Math.abs(yInt - oneIndividual.get(0) * aInt - oneIndividual.get(1) * bInt -
                oneIndividual.get(2) * cInt - oneIndividual.get(3) * dInt);
    }
}