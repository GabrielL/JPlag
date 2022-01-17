package de.jplag.chars;

import de.jplag.ErrorConsumer;
import de.jplag.Language;
import de.jplag.LanguageProvider;
import org.kohsuke.MetaInfServices;

@MetaInfServices
public class CharsLanguageProvider implements LanguageProvider {
    @Override
    public Language create(final ErrorConsumer errorCollector) {
        return new de.jplag.chars.Language(errorCollector);
    }

    @Override
    public String getDisplayName() {
        return "char";
    }
}
