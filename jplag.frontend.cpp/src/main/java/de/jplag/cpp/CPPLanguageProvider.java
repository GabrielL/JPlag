package de.jplag.cpp;

import de.jplag.ErrorConsumer;
import de.jplag.Language;
import de.jplag.LanguageProvider;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class CPPLanguageProvider implements LanguageProvider {
    @Override
    public Language create(final ErrorConsumer errorCollector) {
        return new de.jplag.cpp.Language(errorCollector);
    }

    @Override
    public String getDisplayName() {
        return "cpp";
    }
}
