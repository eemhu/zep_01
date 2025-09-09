/*
 * Teragrep DPL Spark Integration PTH-07
 * Copyright (C) 2022  Suomen Kanuuna Oy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://github.com/teragrep/teragrep/blob/main/LICENSE>.
 *
 *
 * Additional permission under GNU Affero General Public License version 3
 * section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it
 * with other code, such other code is not for that reason alone subject to any
 * of the requirements of the GNU Affero GPL version 3 as long as this Program
 * is the same Program as licensed from Suomen Kanuuna Oy without any additional
 * modifications.
 *
 * Supplemented terms under GNU Affero General Public License version 3
 * section 7
 *
 * Origin of the software must be attributed to Suomen Kanuuna Oy. Any modified
 * versions must be marked as "Modified version of" The Program.
 *
 * Names of the licensors and authors may not be used for publicity purposes.
 *
 * No rights are granted for use of trade names, trademarks, or service marks
 * which are in The Program if any.
 *
 * Licensee must indemnify licensors and authors for any liability that these
 * contractual assumptions impose on licensors and authors.
 *
 * To the extent this program is licensed as part of the Commercial versions of
 * Teragrep, the applicable Commercial License may apply to this file if you as
 * a licensee so wish it.
 */
package com.teragrep.pth_07.ui.elements.table_dynamic;

import jakarta.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DTSearch {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DTSearch.class);
    private final List<String> rowList;

    public DTSearch(List<String> rowList){
        this.rowList = rowList;
    }
    public List<String> search(String searchString){
        List<String> searchedList = new ArrayList<>();
        if (!"".equals(searchString)) {
            try {
                for (String row : rowList) {
                    JsonReader reader = Json.createReader(new StringReader(row));
                    JsonObject line = reader.readObject();

                    // NOTE hard coded to _raw column
                    JsonString _raw = line.getJsonString("_raw");
                    if (_raw != null) {
                        String _rawString = _raw.getString();
                        if (_rawString != null) {
                            if (_rawString.contains(searchString)) {
                                // _raw matches, add whole row to result set
                                searchedList.add(row);
                            }
                        }
                    }
                    reader.close();
                }
                return searchedList;
            } catch (JsonException | IllegalStateException e) {
                LOGGER.error(e.toString());
                return searchedList;
            }
        }
        else {
            searchedList = rowList;
        }
        return searchedList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTSearch dtSearch = (DTSearch) o;
        return Objects.equals(rowList, dtSearch.rowList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowList);
    }
}
