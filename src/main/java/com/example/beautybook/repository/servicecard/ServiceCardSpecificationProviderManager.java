package com.example.beautybook.repository.servicecard;

import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import com.example.beautybook.repository.user.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceCardSpecificationProviderManager
        implements SpecificationProviderManager<ServiceCard> {
    private final List<SpecificationProvider<ServiceCard>> specificationProviders;

    @Override
    public SpecificationProvider<ServiceCard> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found provider by key: " + key));
    }
}
