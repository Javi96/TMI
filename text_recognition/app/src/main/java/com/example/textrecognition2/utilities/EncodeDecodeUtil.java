package com.example.textrecognition2.utilities;

import android.util.Log;

import com.example.textrecognition2.domain.IngrCant;
import com.example.textrecognition2.domain.Plate;

import java.util.ArrayList;

/**
 * <h1>Clase auxiliar para la codificacion/decodificacion de ingredientes y platos</h1>
 */
public final class EncodeDecodeUtil {

    /**
     * Atributos privados
     */
    private static final String DEC_SPLIT_ING_ITEM = "\\*{3}";
    private static final String DEC_SPLIT_ING = "\\_{3}";
    private static final String DEC_SPLIT_PLT = "\\-{3}";

    private static final String SPLIT_ING_ITEM = "***";
    private static final String SPLIT_ING = "___";
    private static final String SPLIT_PLT = "---";

    /**
     * Convierte una lista de ingredientes en un String
     * @param ingredients Lista de ingredientes
     * @return String con la informacion de los ingredientes
     */
    public static StringBuilder encodeIngredients(final ArrayList<IngrCant> ingredients){
        StringBuilder stringBuilder = new StringBuilder();
        for(IngrCant ingredient : ingredients){
            stringBuilder.append(ingredient.getNombre() + SPLIT_ING_ITEM + ingredient.getUnidades() + SPLIT_ING_ITEM + ingredient.getQuantity() + SPLIT_ING);
        }
        return stringBuilder.delete(stringBuilder.length() - SPLIT_ING.length(), stringBuilder.length());
    }

    /**
     * Convierte un StringBuilder en la lista de ingredientes
     * @param stringBuilder Informacion de los ingredientes
     * @return Lista de ingredientes
     */
    public static ArrayList<IngrCant> decodeIngredients(final StringBuilder stringBuilder){
        ArrayList<IngrCant> arrayList = new ArrayList<>();
        String[] ingredients = stringBuilder.toString().split(DEC_SPLIT_ING);
        for(int i=0; i<ingredients.length; i++){
            Log.e("-", ingredients[i]);
            String[] ingredientComp = ingredients[i].split(DEC_SPLIT_ING_ITEM);
            Log.i("--", ingredientComp[0]);
            Log.i("--", ingredientComp[1]);
            Log.i("--", ingredientComp[2]);

            IngrCant ingrCant = new IngrCant(ingredientComp[0], ingredientComp[1], Integer.parseInt(ingredientComp[2]));
            arrayList.add(ingrCant);
        }
        return arrayList;
    }

    /**
     * Convierte una lista de platos en un String
     * @param plates Lista de platos
     * @return String con la informacion de los platos
     */
    public static String encodePlates(final ArrayList<Plate> plates){
        StringBuilder stringBuilder = new StringBuilder();
        for(Plate plate : plates){
            stringBuilder.append(plate.getName() + SPLIT_ING + encodeIngredients(plate.getIngredients()) + SPLIT_PLT);
        }
        stringBuilder.delete(stringBuilder.length() - SPLIT_PLT.length(), stringBuilder.length());
        return stringBuilder.toString();
    }

    /**
     * Convierte un String en la lista de platos
     * @param string Informacion de los platos
     * @return Lista de platos
     */
    public static ArrayList<Plate> decodePlates(final String string){
        ArrayList<Plate> arrayList = new ArrayList<>();
        String[] plates = string.split(DEC_SPLIT_PLT);
        for(int i=0; i<plates.length; i++){
            Log.e("-", plates[i]);
            String[] plateInfo = plates[i].split(DEC_SPLIT_ING);
            StringBuilder stringBuilderAux = new StringBuilder();
            for(int j=1; j<plateInfo.length; j++){
                Log.i("-", plateInfo[j]);
                stringBuilderAux.append(plateInfo[j] + SPLIT_ING);
            }
            stringBuilderAux.delete(stringBuilderAux.length() - SPLIT_ING.length(), stringBuilderAux.length());
            arrayList.add(new Plate(plateInfo[0], decodeIngredients(stringBuilderAux)));

        }
        return arrayList;
    }
}
