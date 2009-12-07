package org.osgeye.console.commands.diagnosis;


import static org.osgeye.console.commands.CommandUtils.*;

import java.util.List;

import org.osgeye.console.BundleStore;
import org.osgeye.console.commands.AbstractExecuteOnBundlesCommand;
import org.osgeye.console.commands.CommandCategory;
import org.osgeye.console.commands.InvalidCommandException;
import org.osgeye.domain.Bundle;
import org.osgeye.domain.BundleState;
import org.osgeye.domain.manifest.ImportPackagesDeclaration;
import org.osgeye.utils.Pair;

public class UnresolvedCommand extends AbstractExecuteOnBundlesCommand
{
  private BundleStore bundleStore;
  private DiagnosisUtils diagnosisUtils;
  
  public UnresolvedCommand(BundleStore bundleStore)
  {
    super(BundleState.INSTALLED);
    this.bundleStore = bundleStore;
    diagnosisUtils = new DiagnosisUtils(bundleStore);
  }

  @Override
  public CommandCategory getCategory()
  {
    return CommandCategory.DIAGNOSIS;
  }

  @Override
  public String getName()
  {
    return "unresolved";
  }

  @Override
  public String getShortDescription()
  {
    return "Attempts to diagnose why bundles in the INSTALLED state weren't resolved.";
  }

  @Override
  protected void executeOnBundles(List<Bundle> matchingBundles, List<String> subcommands) throws InvalidCommandException
  {
    assertEmpty(subcommands);
    
    boolean firstBundle = true;
    for (Bundle bundle : matchingBundles)
    {
      if (!firstBundle) printer.println();
      firstBundle = false;
      
      printer.println(bundle);
      printer.pushIndent();
      boolean foundIssue = false;
      
      List<Pair<ImportPackagesDeclaration, String>> missingPackages = diagnosisUtils.findMissingImports(bundle, true, false);
      if (missingPackages.size() > 0)
      {
        foundIssue = true;
        printer.println("Missing Mandatory Package Imports:");
        printer.pushIndent();

        for (Pair<ImportPackagesDeclaration, String> missingPackage : missingPackages)
        {
          printer.println(missingPackage.y + " " + missingPackage.x.getVersion());
        }

        printer.popupIndent();
      }
      
      List<UsesConflict> usesConflicts = diagnosisUtils.findUsesConflicts(bundle);
      if (usesConflicts.size() > 0)
      {
        foundIssue = true;
        printer.println("Possible Uses Conflicts:");
        printer.pushIndent();
        
        for (UsesConflict usesConflict : usesConflicts)
        {
          printer.println(usesConflict.exportedImport.y + " " + usesConflict.exportedImport.x.getVersion());
          printer.pushIndent();
          
          printer.println("Exporting Bundle: " + usesConflict.exportBundle);
          printer.println("Exported Package: " + usesConflict.exportedPackage);
          printer.println("Exported Package Uses: " + usesConflict.usesWiredExport);
          printer.println("Conflicting Import: " + usesConflict.usesConflictImport.y + " " + usesConflict.usesConflictImport.x.getVersion());
          
          printer.popupIndent();
        }
        
        printer.popupIndent();
      }
      
      if (!foundIssue)
      {
        printer.println("No Issues Found");
      }
      
      printer.popupIndent();
    }
  }
}
