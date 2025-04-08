// package com.yada.util;

// import java.util.*;

// public class AdapterLoader {
//     private final Map<String, FoodDataAdapter> adapters = new HashMap<>();
    
//     public AdapterLoader() {
//         registerAdapter(new McDonaldsAdapter());
//         registerAdapter(new USDAAdapter());
//     }
    
//     public void registerAdapter(FoodDataAdapter adapter) {
//         adapters.put(adapter.getClass().getSimpleName(), adapter);
//     }
    
//     public FoodDataAdapter getAdapter(String source) {
//         return adapters.values().stream()
//             .filter(a -> a.supports(source))
//             .findFirst()
//             .orElseThrow(() -> new NoSuchElementException("No adapter for " + source));
//     }
// }