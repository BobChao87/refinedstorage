package refinedstorage.apiimpl;

import refinedstorage.api.IAPI;
import refinedstorage.api.autocrafting.registry.ICraftingTaskRegistry;
import refinedstorage.api.solderer.ISoldererRegistry;
import refinedstorage.apiimpl.autocrafting.registry.CraftingTaskRegistry;
import refinedstorage.apiimpl.solderer.SoldererRegistry;

import javax.annotation.Nonnull;

public class API implements IAPI {
    public static final IAPI INSTANCE = new API();

    private ISoldererRegistry soldererRegistry = new SoldererRegistry();
    private ICraftingTaskRegistry craftingTaskRegistry = new CraftingTaskRegistry();

    @Override
    @Nonnull
    public ISoldererRegistry getSoldererRegistry() {
        return soldererRegistry;
    }

    @Override
    @Nonnull
    public ICraftingTaskRegistry getCraftingTaskRegistry() {
        return craftingTaskRegistry;
    }
}
