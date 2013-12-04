package org.mule.modules.salesforce.bulk;

import java.util.Collection;
import java.util.List;

import org.mule.api.transformer.DataType;
import org.mule.api.transformer.TransformerException;
import org.mule.modules.salesforce.SalesforceUtils;
import org.mule.transformer.AbstractDiscoverableTransformer;
import org.mule.transformer.types.DataTypeFactory;

import com.sforce.soap.partner.SaveResult;

public class SaveResultToBulkOperationTransformer extends AbstractDiscoverableTransformer {
	
	@SuppressWarnings("unchecked")
	private static final DataType<Collection<SaveResult>> SAVE_RESULT_COLLECTION_DATE_TYPE = DataTypeFactory.create(List.class, SaveResult.class);
	
	public SaveResultToBulkOperationTransformer() {
		this.registerSourceType(SAVE_RESULT_COLLECTION_DATE_TYPE);
		this.returnType = SalesforceUtils.BULK_OPERATION_RESULT_DATA_TYPE;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Object doTransform(Object src, String enc) throws TransformerException {
		List<SaveResult> saveResults = (List<SaveResult>) src;
		return SalesforceUtils.saveResultToBulkOperationResult(saveResults);
	}
	
	
}
