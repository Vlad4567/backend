package com.example.beautybook.repository.mastercard;

import com.example.beautybook.model.MasterCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import com.example.beautybook.repository.user.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MasterCardSpecificationProviderManager
        implements SpecificationProviderManager<MasterCard> {
    private final List<SpecificationProvider<MasterCard>> specificationProviders;

    @Override
    public SpecificationProvider<MasterCard> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found provider by key: " + key));
    }
}
