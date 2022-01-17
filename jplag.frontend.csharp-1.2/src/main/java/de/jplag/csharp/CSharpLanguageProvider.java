package de.jplag.csharp;

import de.jplag.ErrorConsumer;
import de.jplag.Language;
import de.jplag.LanguageProvider;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class CSharpLanguageProvider implements LanguageProvider {
    @Override
    public Language create(final ErrorConsumer errorCollector) {
        return new de.jplag.csharp.Language(errorCollector);
    }

    @Override
    public String getDisplayName() {
        return "csharp";
    }
}
